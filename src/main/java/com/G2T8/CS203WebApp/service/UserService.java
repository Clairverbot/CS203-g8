package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.PasswordResetRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;

import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.slf4j.*;

@Service
public class UserService implements UserDetailsService {

    
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private EmailService emailService;


    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordResetRepository passwordResetRepository){
        this.userRepository=userRepository;
        this.passwordResetRepository=passwordResetRepository;
    }
    @Autowired
    public void setEmailService(EmailService emailService){
        this.emailService=emailService;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long ID) {

        return userRepository.findById(ID).map(user -> {
            return user;
        }).orElse(null);

    }

    @Transactional
    public User updateUserVaccinationStatus(Long id, int vaccinationStatus) {
        User user = getUser(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        user.setVaccinationStatus(vaccinationStatus);
        return userRepository.save(user);

    }

    @Transactional
    public User updateUserName(Long id, String name) {
        User user = getUser(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        user.setName(name);
        return userRepository.save(user);
    }

    @Transactional
    public User updatePasswordInUserProfile(Long id, String password) {
        User user = getUser(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);

    }

    @Transactional
    public User updateRole(Long id, String role) {
        User user = getUser(id);
        if (user != null && (role.equals("ROLE_BASIC") || role.equals("ROLE_ADMIN"))) {
            user.setRole(role);
            return userRepository.save(user);
        }
        throw new UserNotFoundException(id);
    }

    @Transactional
    public User updateManagerId(Long id, User manager) {
        User user = getUser(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        user.setManagerUser(manager);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // In our case, username will be email
        User user = findByEmail(email);

        // If there is no user with that email, throw UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Add authorization -- currently only takes in one role as per the User entity
        // specifications
        // Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // authorities.add(new SimpleGrantedAuthority(user.getRole()));

        // Return the Spring Framework User object, not our custom one!
        // return new
        // org.springframework.security.core.userdetails.User(user.getEmail(),
        // user.getPassword(), authorities);
        return new CustomUserDetails(user);
    }

    public User findByEmail(String email) {
        Optional<User> optional = userRepository.findByEmail(email);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    /**
     * Encodes user password and saves user details to database
     * 
     * @param userDetails to save to database
     */
    public User addUser(UserDTO userDetails) {
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setName(userDetails.getName());
        user.setRole(userDetails.getRole());
        user.setPassword(encodedPassword);
        user.setFirstLogin(true);
        return userRepository.save(user);
    }

    /**
     * Creates an employee account for the user
     * 
     * @param userDetails user details of employee account
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = { MessagingException.class, IOException.class })
    public void createEmployeeAccount(UserDTO userDetails) throws MessagingException, IOException {
        userDetails.setPassword(createRandomPassword(10));

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", userDetails.getName());
        templateModel.put("email", userDetails.getEmail());

        User newEmployee = addUser(userDetails);

        PasswordResetToken passToken = generatePasswordResetToken(UUID.randomUUID().toString(), newEmployee);

        templateModel.put("passtoken", passToken.getToken());

        emailService.sendEmailWithTemplate(userDetails.getEmail(),
                "[XXX Employee Management System] Your account has been created!", "new-employee-account.html",
                templateModel);

    }

    private String createRandomPassword(int stringLength) {
        int leftLimit = '0'; 
        int rightLimit = 'z';
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= '9' || i >= 'A') && (i <= 'Z' || i >= 'a')).limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    // ----------------------------------------------
    // RESET PASSWORD FUNCTIONALITY
    // ----------------------------------------------

    /**
     * Creates a password reset token for a particular user The token itself is a
     * UUID string, but the entity contains the user it corresponds to and an expiry
     * date
     * 
     * @param user user entity for whom to create a password reset token for
     * @throws Exception
     */
    @Transactional(rollbackFor = { MessagingException.class, IOException.class })
    public void createPasswordResetTokenForUser(User user) throws Exception {
        String resetToken = UUID.randomUUID().toString();

        PasswordResetToken token = generatePasswordResetToken(resetToken, user);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("token", token.getToken());
        templateModel.put("expiry", token.getExpiryDate());

        emailService.sendEmailWithTemplate(user.getEmail(), "[XXX Employee Management System] Reset your password",
                "reset-password.html", templateModel);

    }

    /**
     * Utility function to generate password reset token Deletes all tokens
     * previously created for that user
     * 
     * @param resetToken
     * @param user
     * @return
     */
    @Transactional
    private PasswordResetToken generatePasswordResetToken(String resetToken, User user) {
        PasswordResetToken token = new PasswordResetToken(resetToken, user);
        passwordResetRepository.deleteByUserAndExpiryDateLessThan(user, token.getExpiryDate());
        return passwordResetRepository.save(token);
    }

    /**
     * Actually resets the password for the user
     * 
     * @param user
     * @param token
     * @param password
     * @return
     * @throws Exception
     */
    @Transactional
    public String resetPasswordForUser(User user, String token, String password) throws Exception {
        String invalidityMessage = validatePasswordResetToken(token);
        if (invalidityMessage == null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
            passwordResetRepository.deleteByToken(token);
        }
        return invalidityMessage;
    }

    /**
     * Function to find user corresponding to a password reset token
     * 
     * @param token
     * @return
     */
    public Optional<User> findUserByPasswordResetToken(final String token) {
        Optional<PasswordResetToken> opt = passwordResetRepository.findByToken(token);
        if (opt.isPresent()) {
            return Optional.ofNullable(opt.get().getUser());
        } else {
            return null;
        }
    }

    /**
     * Validation of password reset token. Checks whether the token is present in
     * the database & whether it's expired or not
     * 
     * @param token
     * @return
     */
    private String validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> opt = passwordResetRepository.findByToken(token);
        if (opt.isPresent()) {
            PasswordResetToken passToken = opt.get();
            logger.info(passToken.getToken());
            final Calendar cal = Calendar.getInstance();
            return passToken.getExpiryDate().before(cal.getTime()) ? "Expired" : null;
        } else {
            return "Invalid Token";
        }

    }

}
