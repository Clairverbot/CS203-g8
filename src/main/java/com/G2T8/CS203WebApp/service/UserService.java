package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.UserRepository;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;

import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private EmailService emailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long ID) {

        return userRepository.findById(ID).map(user -> {
            return user;
        }).orElse(null);

    }

    public User getUserByEmail(String email) {

        Optional<User> b = userRepository.findByEmail(email);
        if (b.isPresent()) {
            User user = b.get();
            return user;
        } else
            return null;

    }

    // should delete as we logging in w email not possible to change

    public User updateUserEmail(Long ID, String email) {
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            user.setEmail(email);
            return userRepository.save(user);
        } else
            return null;

    }

    public User updateUserVaccinationStatus(User user) {
        return userRepository.save(user);

    }

    public User updateUserName(User user) {
        return userRepository.save(user);

    }

    public User updatepassword(User user) {
        return userRepository.save(user);

    }

    public User updaterole(User user) {
        return userRepository.save(user);
    }

    public User updatemanagerid(User user) {
        return userRepository.save(user);
    }

    // public User updateUserTeamID(Long ID, Long TeamID) {
    // Optional<User> b = userRepository.findById(ID);
    // if (b.isPresent()) {
    // User user = b.get();
    // user.setteam(TeamID);
    // return userRepository.save(user);
    // } else
    // return null;

    // }

    public int updateUserRole(Long ID, String role) {

        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_BASIC")) {
            Optional<User> b = userRepository.findById(ID);
            if (b.isPresent()) {
                User user = b.get();
                user.setRole(role);
                userRepository.save(user);
                return 1; // done perfect
            } else {
                return 20; // user not found
            }
        }
        return 10; // user input wrong role
    }

    public int updateUserManagerID(Long ID, Long managerID) {
        Optional<User> b = userRepository.findById(ID);
        Optional<User> managerop = userRepository.findById(managerID);
        if (b.isPresent() && managerop.isPresent()) {
            User user = b.get();
            User manager = managerop.get();
            if (manager.getRole().equals("ROLE_ADMIN")) {
                user.setManagerUser(manager);
                userRepository.save(user);
                return 1;// done perfect
            } else {
                return 10; // bad request bc the supposed inputted manager isnt a manager
            }
        } else
            return 20;// user or manager not found
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
    public void addUser(UserDTO userDetails) {
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setName(userDetails.getName());
        user.setRole(userDetails.getRole());
        user.setPassword(encodedPassword);
        user.setFirstLogin(true);
        userRepository.save(user);
    }

    /**
     * Creates an employee account for the user
     * 
     * @param userDetails user details of employee account
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = { MessagingException.class, IOException.class })
    public void createEmployeeAccount(UserDTO userDetails) throws MessagingException, IOException {
        // userDetails.setPassword(createRandomPassword(10));

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", userDetails.getName());
        templateModel.put("email", userDetails.getEmail());
        templateModel.put("password", userDetails.getPassword());

        addUser(userDetails);

        emailService.sendEmailWithTemplate(userDetails.getEmail(),
                "[XXX Employee Management System] Your account has been created!", "new-employee-account.html",
                templateModel);

    }

    // private String createRandomPassword(int stringLength) {
    // int leftLimit = 48; // numeral '0'
    // int rightLimit = 122; // letter 'z'
    // Random random = new Random();

    // String generatedString = random.ints(leftLimit, rightLimit + 1)
    // .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >=
    // 97)).limit(stringLength)
    // .collect(StringBuilder::new, StringBuilder::appendCodePoint,
    // StringBuilder::append).toString();

    // return generatedString;
    // }

}
