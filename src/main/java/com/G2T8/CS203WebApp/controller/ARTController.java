package com.G2T8.CS203WebApp.controller;

import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.ARTTestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/art")
public class ARTController {
    @Autowired
    public ARTTestResultService artService;
    @Autowired
    public ARTTestResultRepository artRepo;

    @GetMapping("/allArt")
    public List<ARTTestResults> findAllArt() {
        try{
            return artService.getAllResult();
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @RequestMapping("/art/{id}")
    public List<ARTTestResults> findARTByUserId(Long userId) {
        List<ARTTestResults> toReturn;
        try{
            toReturn = artService.getARTbyUserID(userId);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
        return toReturn;
    }

    @RequestMapping("/art/{id}/{date}")
    public ARTTestResults findARTByUserIdAndDate(Long userId, LocalDateTime date) {
        try{
            return artService.getARTbyUserIdAndDate(userId,date);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @PostMapping("/addART/{weeksMonday}/{result}/{date}")
    public ResponseEntity<?> addTemperature(LocalDateTime weeksMonday, boolean result, LocalDateTime date){
        try{
            artService.addART(weeksMonday,result,date);
            return ResponseEntity.ok(null);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

}
