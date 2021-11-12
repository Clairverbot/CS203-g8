package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.G2T8.CS203WebApp.model.JwtRequest;
import com.G2T8.CS203WebApp.model.JwtResponse;
import com.G2T8.CS203WebApp.model.User;

import java.security.Principal;

import com.G2T8.CS203WebApp.configuration.JwtTokenUtil;
import com.G2T8.CS203WebApp.service.UserService;
import com.G2T8.CS203WebApp.model.UserDTO;
import com.G2T8.CS203WebApp.model.CustomUserDetails;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class JwtAuthController {
    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private UserService userDetailsService;

    @Autowired
    public JwtAuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
            UserService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Endpoint for users to login and create a JWT authentication token
     * 
     * @param authenticationRequest JwtRequest object containing user credentials
     * @return JwtResponse containing JWT token
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        final Boolean firstLogin = ((CustomUserDetails) userDetails).getUser().getFirstLogin();

        return ResponseEntity.ok(new JwtResponse(token, firstLogin));
    }

    /**
     * Endpoint to register for a new admin account
     * 
     * @param user UserDTO object containing user details
     * @return HTTP status 201 if successful
     * @throws Exception
     */
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody @Validated UserDTO user) throws Exception {
        userDetailsService.addUser(user);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    /**
     * Endpoint to create a new employee account
     * 
     * @param user           UserDTO object containing user details
     * @param adminPrincipal current logged-in administrator
     * @return HTTP status 201 if successful
     * @throws Exception
     */
    @PostMapping("/employee")
    public User addEmployee(@RequestBody @Validated UserDTO user, Principal adminPrincipal) throws Exception {
        CustomUserDetails userObj = (CustomUserDetails) userDetailsService.loadUserByUsername(adminPrincipal.getName());
        User manager = userObj.getUser();

        return userDetailsService.createEmployeeAccount(user, manager);
    }

    /**
     * Utility method to check validity of credentials
     * 
     * @param username username of user
     * @param password password of user
     * @throws Exception
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}