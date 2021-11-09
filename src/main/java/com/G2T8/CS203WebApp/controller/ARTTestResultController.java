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
public class ARTTestResultController {
    private final ARTTestResultService artService;

    @Autowired
    public ARTTestResultController(ARTTestResultService artService) {
        this.artService = artService;
    }

    /**
     * Endpoint to return all ART Test Results
     * @return List of all ARTTestResults present in the database
     */
    @GetMapping("/")
    public List<ARTTestResult> findAllArt() {
        try {
            return artService.getAllResult();
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    /**
     * Endpoint to return all ART result with a specific userId
     * @param userId of User of which we want to return all ART Test Results of
     * @return List of ARTTestResults belonging to the user with User ID userId
     */
    @RequestMapping("/{userId}")
    public List<ARTTestResult> findARTByUserId(@PathVariable Long userId) {
        List<ARTTestResult> toReturn;
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

    /**
     * Endpoint to add new ART Test Result
     * @param artResult result of user's ART Test (boolean)
     * @param principal account information of current logged in user
     * @return newly created ARTTestResult and Reponse Status
     */
    @PostMapping("/")
    public ResponseEntity<?> addResult(@RequestBody Boolean artResult, Principal principal) {
        try {
            ARTTestResult artTestResults = artService.addART(principal.getName(), artResult);
            return new ResponseEntity<ARTTestResult>(artTestResults, HttpStatus.CREATED);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    /**
     * Endpoint to get number of ART Tests the curent logged in user has 
     * submitted this week
     * @param principal account information of current logged in user
     * @param date
     * @return
     */
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
