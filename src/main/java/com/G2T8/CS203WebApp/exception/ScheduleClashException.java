package com.G2T8.CS203WebApp.exception;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;
import com.G2T8.CS203WebApp.controller.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Error
public class ScheduleClashException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ScheduleClashException() {
        // happens when a team tries to set their schedule to work in office
        // when another team is already in office
        super("Schedule clash occured");
    }
}
