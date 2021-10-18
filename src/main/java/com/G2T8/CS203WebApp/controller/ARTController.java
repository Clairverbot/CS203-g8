package com.G2T8.CS203WebApp.controller;

import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.ARTTestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.format.annotation.*;
import java.time.LocalDateTime;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/art")
public class ARTController {
    @Autowired
    public ARTTestResultService artService;
    @Autowired
    public ARTTestResultRepository artRepo;

    @GetMapping("/")
    public List<ARTTestResults> findAllArt() {
        try{
            return artService.getAllResult();
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @RequestMapping("/{userId}")
    public List<ARTTestResults> findARTByUserId(@PathVariable Long userId) {
        List<ARTTestResults> toReturn;
        try{
            toReturn = artService.getARTbyUserID(userId);
        } catch(UserNotFoundException E){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
        return toReturn;
    }

    @GetMapping("/{userId}/{date}")
    public ARTTestResults findARTByUserIdAndDate(@PathVariable Long userId, @RequestParam("localDateTime") 
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime date) {
        try{
            return artService.getARTbyUserIdAndDate(userId,date);
        } catch(UserNotFoundException E){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addResult(@RequestBody ARTTestResults art){
        try{
            artService.addART(art);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch(UserNotFoundException E){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

}
