package com.G2T8.CS203WebApp.exception;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;
import com.G2T8.CS203WebApp.controller.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class CovidHistoryNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CovidHistoryNotFoundException(Long id) {
        super("Could not find Covid History of user with " + id);
    }

    public CovidHistoryNotFoundException() {
        super("No Covid Histories");
    }
    
}
