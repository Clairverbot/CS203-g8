package com.G2T8.CS203WebApp.controller;

import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/temperature")
public class TemperatureController {
    @Autowired
    public TemperatureService tempService;
    @Autowired
    public TemperatureRepository tempRepo;

    @GetMapping("/allTemp")
    public List<Temperature> findAllTemp() {
        try{
            return tempService.getAllTemp();
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @RequestMapping("/temp/{id}")
    public List<Temperature> findTempByUserId(Long userId) {
        List<Temperature> toReturn;
        try{
            toReturn = tempService.getAllTempbyUserID(userId);
        } catch(NullPointerException E){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "User not found!");
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
        return toReturn;
    }

    @RequestMapping("/temp/{id}/{date}")
    public Temperature findTempByUserIdAndDate(Long userId, LocalDateTime date) {
        try{
            return tempService.getTempbyUserIDAndDate(userId,date);
        } catch(NullPointerException E){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "User not found!");
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @PostMapping("/add/{date}/{temperature}")
    public ResponseEntity<?> addTemperature(LocalDateTime date, double temp){
        try{
            tempService.addTemperature(date,temp);
            return ResponseEntity.ok(null);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

}
