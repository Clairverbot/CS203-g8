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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import com.G2T8.CS203WebApp.model.JwtRequest;
import com.G2T8.CS203WebApp.model.JwtResponse;
import com.G2T8.CS203WebApp.configuration.JwtTokenUtil;
import com.G2T8.CS203WebApp.service.UserService;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.model.UserDTO;

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

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody @Validated UserDTO user) throws Exception {
        userDetailsService.addUser(user);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("/get-profile")
    public ResponseEntity<?> getUserInfo(Principal user) {
        org.springframework.security.core.userdetails.User userObj = (org.springframework.security.core.userdetails.User) userDetailsService
                .loadUserByUsername(user.getName());
        User userEntity = userDetailsService.findByEmail(userObj.getUsername());

        return ResponseEntity.ok(userEntity);
    }

    /**
     * Dummy endpoint to test authorization
     * 
     * @return dummy
     */
    @GetMapping("/dummy")
    public String dummyEndpoint() {
        return "dummy";
    }
}