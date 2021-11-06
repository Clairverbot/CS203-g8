package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.server.ResponseStatusException;
import java.lang.IllegalArgumentException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.UserService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get all users
    @GetMapping("/")
    public List<User> findAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users == null) {
            throw new UserNotFoundException();
        }
        return users;
    }

    // get user by Email ( necessary as we are logging in with email)
    @GetMapping(value = "/email/{email}")
    public User findUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);

        }
        return user;
    }

    // get user by ID
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

    @PutMapping("/{id}/vaccination-status")
    public User updateVaccinationStatus(@PathVariable Long id, @RequestBody int vaccinationStatus) {
        return userService.updateUserVaccinationStatus(id, vaccinationStatus);
    }

    @PutMapping("/{id}/name")
    public User updateName(@PathVariable Long id, @RequestBody String name) {
        return userService.updateUserName(id, name);
    }

    @PutMapping(value = "/{id}/role")
    public User updateRole(@PathVariable Long id, @Valid @RequestBody String role) {
        return userService.updateRole(id, role);
    }

    @PutMapping(value = "/{id}/manager")
    public User updateManagerId(@PathVariable Long id, @Valid @RequestBody Long managerId) {
        return userService.updateManagerId(id, managerId);
    }

    /**
     * Endpoint for generating a reset password token associated with an email
     * 
     * @param email
     * @return
     * @throws Exception
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
     * @param passReset a map object ideally containing token and newPassword.
     *                  TO-DO: Return an invalid reset password token exception if
     *                  the fields are not all filled up
     * @return
     * @throws Exception
     */
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> passReset) throws Exception {
        Optional<User> user = userService.findUserByPasswordResetToken(passReset.get("token").toString());
        String message = null;
        if (user.isPresent()) {
            message = userService.resetPasswordForUser(user.get(), passReset.get("token").toString(),
                    passReset.get("newPassword").toString());
        } else {
            throw new UserNotFoundException();
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);

        if (message == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{userId}/team")
    public void updateTeam(@PathVariable Long userId, @RequestBody Long teamId) {
        try {
            userService.updateUserTeam(userId, teamId);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (IllegalArgumentException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

}