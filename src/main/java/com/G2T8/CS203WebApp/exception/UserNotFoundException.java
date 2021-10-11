package com.G2T8.CS203WebApp.exception;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;
import com.G2T8.CS203WebApp.controller.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(Long id) {
        super("Could not find user with id " + id);
    }

    public UserNotFoundException(String email) {
        super("Could not find email " + email);
    }

    public UserNotFoundException() {
        super("No users");
    }

}
