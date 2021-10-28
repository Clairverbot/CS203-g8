package com.G2T8.CS203WebApp.controller;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.*;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.server.ResponseStatusException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // Get all temperature
    @GetMapping("/")
    public List<Temperature> findAllTemp() {
        try {
            return tempService.getAllTemp();
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    // Get all temperature based on userId
    @GetMapping("/{userId}")
    public List<Temperature> findTempByUserId(@PathVariable Long userId) {
        List<Temperature> toReturn;
        try {
            toReturn = tempService.getAllTempbyUserID(userId);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
        return toReturn;
    }

    // Get temperature based on userId and date
    @GetMapping("/{userId}/{date}")
    public Temperature findTempByUserIdAndDate(@PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            return tempService.getTempbyUserIDAndDate(userId, date);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    /**
     * Get the number of temperature logs of a user on a certain date
     * 
     * @param userId id of the user
     * @param date   string representing the date in YYYY-MM-DD format
     * @return count of temperature logs on a certain date belonging to a certain
     *         user
     */
    @GetMapping("/{userId}/count")
    public int getCountTemperatureLogOnDate(@PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String date) {
        try {
            LocalDate day = LocalDate.parse(date);
            return tempService.getUserTempOnDay(userId, day).size();
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (DateTimeParseException E) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Input needs to be of a valid date format (YYYY-MM-DD)");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    // Add temperature
    @PostMapping("/")
    public ResponseEntity<?> addTemp(@RequestBody double temperature, Principal principal) {
        try {
            Temperature responseBody = tempService.addTemperature(principal.getName(), temperature);
            return new ResponseEntity<Temperature>(responseBody, HttpStatus.CREATED);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

}
