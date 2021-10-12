package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.UserService;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    // done
    // get all users
    @GetMapping("/findAll")
    public List<User> findAllUsers() {
        if (userService.getAllUsers() != null) {
            return userService.getAllUsers();
        }
        throw new UserNotFoundException();

    }

    // done
    // get user by Email ( necessary as we are logging in with email)
    @RequestMapping(value = "/findUserByEmail/{email}", method = RequestMethod.GET)
    public User findUserByEmail(@PathVariable(value = "email") String email) {
        if (userService.findByEmail(email) != null) {
            return userService.findByEmail(email);
        } else {
            throw new UserNotFoundException(email);

        }

    }

    // done
    // get user by ID
    @GetMapping(value = { "/getProfile/{ID}", "/getProfile" })
    public User findUserByID(@PathVariable(value = "ID", required = false) Long ID, Principal principal) {
        if (ID == null) {
            org.springframework.security.core.userdetails.User userObj = (org.springframework.security.core.userdetails.User) userService
                    .loadUserByUsername(principal.getName());
            User userEntity = userService.findByEmail(userObj.getUsername());

            if (userEntity == null) {
                throw new UserNotFoundException();
            }
            return userEntity;

        } else {
            if (userService.getUser(ID) != null) {
                return userService.getUser(ID);
            } else {
                throw new UserNotFoundException(ID);
            }
        }
    }

    // done
    // updatesVaccinationStatus
    @PutMapping("/updateVaccinationStatus/{id}")
    public User updateVaccinationStatus(@PathVariable Long id, @Valid @RequestBody User userLatest) {
        Optional<User> userop = userRepo.findById(id);
        if (userop.isPresent()) {
            User userReal = userop.get();
            userReal = userLatest;
            return userRepo.save(userReal);

        }
        throw new UserNotFoundException(id);

    }

    // done
    // updates password
    @RequestMapping(value = { "/updatepassword/{id}", "/updatepassword" }, method = RequestMethod.PUT)
    public User updatePassword(@PathVariable Long id, @Valid @RequestBody User userLatest) {
        Optional<User> userop = userRepo.findById(id);
        if (userop.isPresent()) {
            User userReal = userop.get();
            userReal = userLatest;
            return userRepo.save(userReal);
        }
        throw new UserNotFoundException(id);
    }

    // done
    // updates name
    @RequestMapping(value = "/updatename/{id}", method = RequestMethod.PUT)
    public User updateName(@PathVariable Long id, @Valid @RequestBody User userLatest) {
        Optional<User> userop = userRepo.findById(id);
        if (userop.isPresent()) {
            User userReal = userop.get();
            userReal = userLatest;
            return userRepo.save(userReal);
        }
        throw new UserNotFoundException(id);
    }

    // done
    // updates role
    @RequestMapping(value = "/updaterole/{id}", method = RequestMethod.PUT)
    public User updateRole(@PathVariable Long id, @Valid @RequestBody User userLatest) {
        Optional<User> userop = userRepo.findById(id);
        if (userop.isPresent()
                && (userLatest.getRole().equals("ROLE_BASIC") || userLatest.getRole().equals("ROLE_ADMIN"))) {
            User userReal = userop.get();
            userReal = userLatest;
            return userRepo.save(userReal);
        }
        throw new UserNotFoundException(id);
    }

    // updates managerid
    @RequestMapping(value = "/updatemanagerid/{id}", method = RequestMethod.PUT)
    public User updateManagerId(@PathVariable Long id, @Valid @RequestBody User userLatest) {
        Optional<User> userop = userRepo.findById(id);
        if (userop.isPresent()) {
            User userReal = userop.get();
            userReal = userLatest;
            return userRepo.save(userReal);
        }
        throw new UserNotFoundException(id);
    }

    // decided not to do this bc ur logging in w the email
    // // change user email
    // @PutMapping("/updateEmail/{ID}/{email}")
    // public ResponseEntity<String> updateEmail(@PathVariable("ID") Long ID,
    // @PathVariable("email") String email) {

    // User user = userService.updateUserEmail(ID, email);
    // if (user != null) {
    // return new ResponseEntity<>(HttpStatus.OK);
    // }
    // throw new UserNotFoundException(ID);
    // }

    // //change managerid of a user
    // @PutMapping("/updateManagerID/{ID}/{managerID}")
    // public ResponseEntity<String> updateManagerID(@PathVariable("ID") Long ID,
    // @PathVariable("managerID") Long managerID) {

    // int result = userService.updateUserManagerID(ID, managerID);
    // if (result != 20) {
    // if(result == 10){
    // // suppposed manager inputted is not a manager
    // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    // }
    // // if 1 is returned means user and manager exist and manager is manager
    // return new ResponseEntity<>(HttpStatus.OK);
    // }
    // throw new UserNotFoundException(ID);
    // }

    /**
     * Endpoint for generating a reset password token associated with an email
     * 
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("/reset-password-token")
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

}