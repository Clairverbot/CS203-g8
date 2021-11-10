package com.G2T8.CS203WebApp.controller;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.G2T8.CS203WebApp.exception.TemperatureInvalidException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/temperature")
public class TemperatureController {
    private final TemperatureService tempService;

    @Autowired
    public TemperatureController(TemperatureService tempService) {
        this.tempService = tempService;
    }

    /**
     * Get all temperature logs of a user with a specific ID
     * 
     * @param userId
     * @return List of temperature by user
     */
    @GetMapping("/{userId}")
    public List<Temperature> findTempByUserId(@PathVariable Long userId) {
        return tempService.getAllTempbyUserID(userId);
    }

    /**
     * Get all user temperature logs by a certain day using email
     * 
     * @param email
     * @param day
     * @return list of user temperature logs by a certain day using email
     */
    @GetMapping("/current/count")
    public int getCurrentUserCountTemperatureLogOnDate(Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String date) {
        try {
            LocalDate day = LocalDate.parse(date);
            return tempService.getUserTempOnDayByUserEmail(principal.getName(), day).size();
        } catch (DateTimeParseException E) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Input needs to be of a valid date format (YYYY-MM-DD)");
        }
    }

    /**
     * Method to add temperature log
     * 
     * @param email
     * @param temperature
     * @return added temperature
     */
    @PostMapping("/")
    public ResponseEntity<?> addTemp(@RequestBody double temperature, Principal principal) {
        try {
            Temperature responseBody = tempService.addTemperature(principal.getName(), temperature);
            return new ResponseEntity<Temperature>(responseBody, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new TemperatureInvalidException(temperature);
        }
    }

}
