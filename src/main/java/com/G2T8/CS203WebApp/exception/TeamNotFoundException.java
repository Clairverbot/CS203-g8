package com.G2T8.CS203WebApp.exception;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;
import com.G2T8.CS203WebApp.controller.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(Long id) {
        super("Could not find team with id " + id);
    }
}
