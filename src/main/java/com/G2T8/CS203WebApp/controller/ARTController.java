package com.G2T8.CS203WebApp.controller;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.ARTTestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.format.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/art")
public class ARTController {
    private final ARTTestResultService artService;

    @Autowired
    public ARTController(ARTTestResultService artService) {
        this.artService = artService;
    }

    // Get all ART results
    @GetMapping("/")
    public List<ARTTestResults> findAllArt() {
        try {
            return artService.getAllResult();
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    // Get all ART result based on userId
    @RequestMapping("/{userId}")
    public List<ARTTestResults> findARTByUserId(@PathVariable Long userId) {
        List<ARTTestResults> toReturn;
        try {
            toReturn = artService.getARTbyUserID(userId);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
        return toReturn;
    }

    // Add ART result
    @PostMapping("/")
    public ResponseEntity<?> addResult(@RequestBody Boolean artResult, Principal principal) {
        try {
            ARTTestResults artTestResults = artService.addART(principal.getName(), artResult);
            return new ResponseEntity<ARTTestResults>(artTestResults, HttpStatus.CREATED);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    @GetMapping("/current/count-on-week")
    public int getCountCurrentUserARTResultOnWeek(Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String date) {
        try {
            LocalDateTime dateTime = LocalDate.parse(date).atStartOfDay();
            return artService.getARTbyUserEmailAndWeek(principal.getName(), dateTime).size();
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

}
