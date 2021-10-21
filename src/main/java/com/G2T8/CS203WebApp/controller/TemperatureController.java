package com.G2T8.CS203WebApp.controller;

import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/temperature")
public class TemperatureController {
    @Autowired
    public TemperatureService tempService;
    @Autowired
    public TemperatureRepository tempRepo;

    @GetMapping("/")
    public List<Temperature> findAllTemp() {
        try {
            return tempService.getAllTemp();
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

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

    @GetMapping("/{userId}/{date}")
    public Temperature findTempByUserIdAndDate(@PathVariable Long userId,
            @RequestParam("localDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            return tempService.getTempbyUserIDAndDate(userId, date);
        } catch (UserNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

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
