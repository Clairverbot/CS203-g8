package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.validation.Valid;

import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    // get all users
    @GetMapping("/findAll")
    public List<User> findAllUsers() {
        if (userService.getAllUsers() != null) {
            return userService.getAllUsers();
        }
        // actually supp to throw user not found exception
        throw new UserNotFoundException();

    }

    // get user by Email ( necessary as we are logging in with email)
    @RequestMapping("/email/{email}")
    public User findUserByEmail(String email) {
        if (userService.getUserByEmail(email) != null) {
            return userService.getUserByEmail(email);
        } else {
            // actually supp to throw user not found exception
            throw new UserNotFoundException(email);

        }

    }

    // get user by ID
    @RequestMapping("/ID/{ID}")
    public User findUserByID(Long ID) {
        if (userService.getUser(ID) != null) {
            return userService.getUser(ID);
        } else {
            // actually supp to throw user not found exception
            throw new UserNotFoundException(ID);

        }
    }

    // change a persons vaccination status
    @PutMapping("/updateVaccinationStatus/{ID}/{vaccinationstatus}")
    public ResponseEntity<String> updateVaccinationStatus(@PathVariable("ID") Long ID,
            @PathVariable("vaccinationstatus") int vaccinationstatus) {

        int result = userService.updateUserVaccinationStatus(ID, vaccinationstatus);
        if (result != 20) {
            if (result == 10) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }

        }
        //// actually supp to throw user not found exception
        throw new UserNotFoundException(ID);

    }

    // change user email
    @PutMapping("/updateEmail/{ID}/{email}")
    public ResponseEntity<String> updateEmail(@PathVariable("ID") Long ID, @PathVariable("email") String email) {

        User user = userService.updateUserEmail(ID, email);
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // // actually supp to throw user not found exception
        throw new UserNotFoundException(ID);
    }

    // change user password

    // change name of user
    @PutMapping("/updateName/{ID}/{name}")
    public ResponseEntity<String> updateName(@PathVariable("ID") Long ID, @PathVariable("name") String name) {

        User user = userService.updateUserEmail(ID, name);
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // actually supp to throw user not found exception
        throw new UserNotFoundException(ID);
    }

    // change role of user
    @PutMapping("/updateRole/{ID}/{role}")
    public ResponseEntity<String> updateRole(@PathVariable("ID") Long ID, @PathVariable("role") String role) {

        int result = userService.updateUserRole(ID, role);
        if (result == 1) {// done ok
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (result == 10) {// user input invalid role like officer
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // if result == 20 and user not there
        // actually supp to throw user not found exception
        throw new UserNotFoundException(ID);
    }

    // change managerid of a user
    @PutMapping("/updateManagerID/{ID}/{managerID}")
    public ResponseEntity<String> updateManagerID(@PathVariable("ID") Long ID,
            @PathVariable("managerID") Long managerID) {

        int result = userService.updateUserManagerID(ID, managerID);
        if (result != 20) {
            if (result == 10) {
                // suppposed manager inputted is not a manager
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // if 1 is returned means user and manager exist and manager is manager
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // actually supp to throw user not found exception
        throw new UserNotFoundException(ID);
    }

}