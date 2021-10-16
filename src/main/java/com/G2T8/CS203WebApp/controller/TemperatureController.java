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
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/temperature")
public class TemperatureController {
    @Autowired
    public TemperatureService tempService;
    @Autowired
    public TemperatureRepository tempRepo;
    @Autowired
    public UserRepository userRepo;

    @GetMapping("/allTemp")
    public List<Temperature> findAllTemp() {
        try{
            return tempService.getAllTemp();
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @GetMapping("/temp/{userId}")
    public List<Temperature> findTempByUserId(@PathVariable Long userId) {
        Optional<User> user = userRepo.findById(userId);
        if(!user.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }
        List<Temperature> toReturn;
        try{
            toReturn = tempService.getAllTempbyUserID(userId);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
        return toReturn;
    }

    @GetMapping("/temp/{userId}/{date}")
    public Temperature findTempByUserIdAndDate(@PathVariable Long userId, @RequestParam("localDateTime") 
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        Optional<User> user = userRepo.findById(userId);
        if(!user.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }
        try{
            return tempService.getTempbyUserIDAndDate(userId,date);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @PostMapping("/addTemp")
    public ResponseEntity<?> addTemp(@RequestBody Temperature temperature){
        Optional<User> user = userRepo.findById(temperature.getUser().getID());
        if(!user.isPresent() || user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }
        try{
            tempService.addTemperature(temperature);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

}
