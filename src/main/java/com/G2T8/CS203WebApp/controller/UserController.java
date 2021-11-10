package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.G2T8.CS203WebApp.exception.InvalidPasswordResetTokenException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Find all users
     * 
     * @return list of all users registered in the database
     */
    @GetMapping("/")
    public List<User> findAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users == null) {
            throw new UserNotFoundException();
        }
        return users;
    }

    /**
     * Get a specific user by ID
     * 
     * @param id user ID
     * @return specific user associated with that ID
     */
    @GetMapping(value = "/{id}")
    public User findUserByID(@PathVariable(value = "id") Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    /**
     * Get current logged-in user's information
     * 
     * @param principal Spring security principal object of current logged-in user
     * @return user entity
     */
    @GetMapping("/current")
    public User getCurrentUser(Principal principal) {
        UserDetails userObj = (UserDetails) userService.loadUserByUsername(principal.getName());
        User userEntity = userService.findByEmail(userObj.getUsername());

        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        return userEntity;
    }

    /**
     * Get percentage of users vaccinated
     * 
     * @return percentage of users vaccinated in integer (0-100)
     */
    @GetMapping("/vaccination-percentage")
    public int findUsersVaxxPercentage() {
        return userService.getUsersVaxxPercentage();
    }

    /**
     * Update a certain user's vaccination status
     * 
     * @param id                user ID
     * @param vaccinationStatus new vaccination status to update
     * @return updated user entity
     */
    @PutMapping("/{id}/vaccination-status")
    public User updateVaccinationStatus(@PathVariable Long id, @RequestBody int vaccinationStatus) {
        return userService.updateUserVaccinationStatus(id, vaccinationStatus);
    }

    /**
     * Update a certain user's name
     * 
     * @param id   user ID
     * @param name new user name
     * @return updated user entity
     */
    @PutMapping("/{id}/name")
    public User updateName(@PathVariable Long id, @RequestBody String name) {
        return userService.updateUserName(id, name);
    }

    /**
     * Update a certain user's role
     * 
     * @param id   user ID
     * @param role new user role: either ROLE_BASIC or ROLE_ADMIN
     * @return updated user entity
     */
    @PutMapping(value = "/{id}/role")
    public User updateRole(@PathVariable Long id, @Valid @RequestBody String role) {
        return userService.updateRole(id, role);
    }

    /**
     * Update a certain user's manager
     * 
     * @param id        user ID
     * @param managerId ID of manager user
     * @return updated user entity
     */
    @PutMapping(value = "/{id}/manager")
    public User updateManagerId(@PathVariable Long id, @RequestBody Long managerId) {
        return userService.updateManagerId(id, managerId);
    }

    /**
     * Update a certain user's team
     * 
     * @param id     user ID
     * @param teamId ID of team
     */
    @PutMapping("/{id}/team")
    public void updateTeam(@PathVariable Long id, @RequestBody Long teamId) {
        userService.updateUserTeam(id, teamId);
    }

    /**
     * Endpoint for generating a reset password token associated with an email
     * 
     * @param email email of user account
     * @return HTTP status CREATED if succeeded
     * @throws Exception mailing exception, UserNotFoundException if user is not
     *                   found
     */
    @PostMapping("/reset-password/token")
    public ResponseEntity<?> getResetPasswordToken(@RequestBody String email) throws Exception {
        User user = userService.findByEmail(email.trim());

        if (user == null) {
            throw new UserNotFoundException(email);
        }

        userService.createPasswordResetTokenForUser(user);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    /**
     * Endpoint for actually resetting the password
     * 
     * @param passReset a map object containing token and newPassword.
     * @return HTTP status OK if request goes through
     * @throws Exception InvalidPasswordResetTokenException if invalid password
     *                   reset token, UserNotFoundException if user is not found
     */
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Validated PasswordResetDTO passReset, BindingResult errors)
            throws Exception {
        Optional<User> user = userService.findUserByPasswordResetToken(passReset.getToken());
        if (errors.hasFieldErrors("token")) {
            throw new InvalidPasswordResetTokenException("Token from the request body is invalid.");
        }
        if (errors.hasFieldErrors("newPassword")) {
            throw new InvalidPasswordResetTokenException("New password from the request body is invalid.");
        }
        if (user.isPresent()) {
            userService.resetPasswordForUser(user.get(), passReset.getToken(), passReset.getNewPassword());
        } else {
            throw new UserNotFoundException();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}