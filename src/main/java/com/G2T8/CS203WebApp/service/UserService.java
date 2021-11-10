package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.PasswordResetRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;

import com.G2T8.CS203WebApp.exception.InvalidPasswordResetTokenException;
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
    private TeamService teamService;
    private CovidHistoryService covidHistoryService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordResetRepository passwordResetRepository) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setCovidHistoryService(CovidHistoryService covidHistoryService) {
        this.covidHistoryService = covidHistoryService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Retrieves all users from the repository
     * 
     * @return list of all users from the repository
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a specific user by index
     * 
     * @param id user ID
     * @return user entity associated with that id
     */
    public User getUser(Long id) {
        return userRepository.findById(id).map(user -> {
            return user;
        }).orElseThrow(() -> {
            throw new UserNotFoundException(id);
        });
    }

    /**
     * Get list of users who currently have Covid
     * 
     * @return list of users who currently have covid
     */
    public List<User> getContractedUsers() {
        List<User> userList = userRepository.findAll();
        List<User> toReturn = new ArrayList<User>();
        for (User u : userList) {
            Long id = u.getID();
            boolean recovery = true;
            List<CovidHistory> history = covidHistoryService.getAllCovidHistoryFromOneUser(id);
            for (CovidHistory c : history) {
                if (!c.recovered()) {
                    recovery = false;
                    break;
                }
            }
            if (!recovery) {
                toReturn.add(u);
            }
        }
        return toReturn;
    }

    // ----------------------------------------------
    // UPDATE USER INFORMATION
    // ----------------------------------------------

    /**
     * Updates vaccination status of user
     * 
     * @param id                user id
     * @param vaccinationStatus new vaccination status of the user
     * @return updated user entity
     */
    @Transactional
    public User updateUserVaccinationStatus(Long id, int vaccinationStatus) {
        User user = getUser(id);
        boolean isValidStatus = vaccinationStatus == 0 || vaccinationStatus == 1 || vaccinationStatus == 2;
        if (isValidStatus) {
            user.setVaccinationStatus(vaccinationStatus);
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Vaccination Status must be 0, 1, or 2");
        }

    }

    /**
     * Update name of user
     * 
     * @param id   user id
     * @param name new name of user
     * @return updated user entity
     */
    @Transactional
    public User updateUserName(Long id, String name) {
        User user = getUser(id);
        user.setName(name);
        return userRepository.save(user);
    }

    /**
     * Update password of user
     * 
     * @param id       user id
     * @param password new plain-text password
     * @return updated user
     */
    @Transactional
    public User updatePasswordInUserProfile(Long id, String password) {
        User user = getUser(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);

    }

    /**
     * Update role of user
     * 
     * @param id   user id
     * @param role new role
     * @return updated user entity
     */
    @Transactional
    public User updateRole(Long id, String role) {
        User user = getUser(id);
        if (role.equals("ROLE_BASIC") || role.equals("ROLE_ADMIN")) {
            user.setRole(role);
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Role must be ROLE_BASIC or ROLE_ADMIN");
        }
    }

    /**
     * Update manager of user
     * 
     * @param id        user id
     * @param managerId user id of manager
     * @return updated user entity
     */
    @Transactional
    public User updateManagerId(Long id, Long managerId) {
        if (id.equals(managerId)) {
            throw new IllegalArgumentException("Manager cannot have the same ID as user");
        }
        User user = getUser(id);
        User manager = managerId == null ? null : getUser(managerId);
        if (manager != null && !manager.getRole().equals("ROLE_ADMIN")) {
            throw new UserNotFoundException(managerId);
        }
        user.setManagerUser(manager);
        return userRepository.save(user);
    }

    /**
     * Update team of user
     * 
     * @param userId user id
     * @param teamId team id
     * @return updated user entity
     */
    @Transactional
    public User updateUserTeam(Long userId, Long teamId) {
        User user = getUser(userId);
        Team team = teamService.getTeam(teamId);
        user.setTeam(team);
        return userRepository.save(user);
    }

    // ---------------- End of update user info methods ----------------------

    /**
     * Overrides loadUserByUsername method from Spring Security's UserDetails
     * 
     * @param email email of the user (username)
     * @return CustomUserDetails (child of Spring Security's UserDetails with the
     *         user entity wrapped inside it)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // In our case, username will be email
        User user = findByEmail(email);

        // If there is no user with that email, throw UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new CustomUserDetails(user);
    }

    /**
     * Retrieves a user from database by email
     * 
     * @param email email of the user
     * @return user object, else null
     */
    public User findByEmail(String email) {
        Optional<User> optional = userRepository.findByEmail(email);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    /**
     * Get percentage of users vaccinated
     * 
     * @return percentage of users vaccinated in integer (e.g. 95% means it will
     *         return 95)
     */
    public int getUsersVaxxPercentage() {
        List<User> userList = userRepository.findAll();
        int countUser = 0;
        int countVaxx = 0;
        for (User u : userList) {
            if (u.isVaccinated()) {
                countVaxx++;
            }
            countUser++;
        }
        return (int) ((double) countVaxx / (double) countUser * 100);
    }

    // ----------------------------------------------
    // SAVE USER INTO DATABASE
    // ----------------------------------------------

    /**
     * Saves user details to database
     * 
     * @param userDetails to save to database
     * @return the created user entity
     */
    public User addUser(UserDTO userDetails) {
        User user = setUserData(userDetails);
        return userRepository.save(user);
    }

    /**
     * Overloaded method: Saves user details and the manager who created that user
     * into the database
     * 
     * @param userDetails details of the user account
     * @param manager     manager who created the user account
     * @return the created user entity
     */
    public User addUser(UserDTO userDetails, User manager) {
        User user = setUserData(userDetails);
        user.setManagerUser(manager);
        return userRepository.save(user);
    }

    /**
     * Helper method: Sets user information according to supplied user details
     * 
     * @param userDetails user details
     * @return user entity with set information
     */
    private User setUserData(UserDTO userDetails) {
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setName(userDetails.getName());
        user.setRole(userDetails.getRole());
        user.setPassword(encodedPassword);
        user.setFirstLogin(true);
        return user;
    }

    /**
     * Creates an employee account for the user
     * 
     * @param userDetails user details of employee account
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = { MessagingException.class, IOException.class })
    public User createEmployeeAccount(UserDTO userDetails, User manager) throws MessagingException, IOException {
        userDetails.setPassword(createRandomPassword(10));

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", userDetails.getName());
        templateModel.put("email", userDetails.getEmail());

        User newEmployee = addUser(userDetails, manager);

        PasswordResetToken passToken = generatePasswordResetToken(UUID.randomUUID().toString(), newEmployee);

        templateModel.put("passtoken", passToken.getToken());

        emailService.sendEmailWithTemplate(userDetails.getEmail(),
                "[XXX Employee Management System] Your account has been created!", "new-employee-account.html",
                templateModel);

        return newEmployee;

    }

    /**
     * Utility method: creates a random alphanumeric string for default password
     * 
     * @param stringLength length of generated string
     * @return random alphanumeric string
     */
    private String createRandomPassword(int stringLength) {
        int leftLimit = '0';
        int rightLimit = 'z';
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= '9' || i >= 'A') && (i <= 'Z' || i >= 'a'))
                .limit(stringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // ------------ End of save user into database methods ------------

    // ----------------------------------------------
    // RESET PASSWORD FUNCTIONALITY
    // ----------------------------------------------

    /**
     * Creates a password reset token for a particular user. The token itself is a
     * UUID string, but the entity contains the user it corresponds to and an expiry
     * date
     * 
     * @param user user entity for whom to create a password reset token for
     * @throws Exception fail to email
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
     * Utility function to generate password reset token, Deletes all tokens
     * previously created for that user
     * 
     * @param resetToken password reset token string
     * @param user       user to generate the token for
     * @return PasswordResetToken object that got saved into DB
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
     * @param user     the user entity to reset password for
     * @param token    the password reset token
     * @param password the new password
     * @throws Exception InvalidPasswordResetToken exception propagated from
     *                   validatePasswordResetToken method
     */
    @Transactional
    public void resetPasswordForUser(User user, String token, String password) throws Exception {
        validatePasswordResetToken(token);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
        passwordResetRepository.deleteByToken(token);
    }

    /**
     * Function to find user corresponding to a password reset token
     * 
     * @param token password reset token
     * @return optional containing the user entity corresponding to that token
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
     * @param token password reset token
     * @return true if token valid
     */
    private boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> opt = passwordResetRepository.findByToken(token);
        if (opt.isPresent()) {
            PasswordResetToken passToken = opt.get();
            final Calendar cal = Calendar.getInstance();
            if (passToken.getExpiryDate().before(cal.getTime())) {
                throw new InvalidPasswordResetTokenException("Password reset token expired");
            } else {
                return true;
            }
        } else {
            throw new InvalidPasswordResetTokenException("Password reset token is invalid");
        }

    }

}
