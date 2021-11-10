package com.G2T8.CS203WebApp.exception;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;
import java.time.LocalDate;
import com.G2T8.CS203WebApp.controller.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class ScheduleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ScheduleNotFoundException() {
        super("No schedule found");
    }

    public ScheduleNotFoundException(Long teamID) {
        super("Could not find Schedule with Team ID " + teamID);
    }

    public ScheduleNotFoundException(Long teamID, LocalDate startDateTime) {
        super("Could not find Schedule with Team ID " + teamID + "and Start Date Time " + startDateTime);
    }
}
