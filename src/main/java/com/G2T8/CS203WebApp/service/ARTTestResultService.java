package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@Service
public class ARTTestResultService {
    @Autowired
    private ARTTestResultRepository artTestResultRepository;

    public List<ARTTestResults> getAllResult(){
        try{
            return artTestResultRepository.findAll();
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    public List<ARTTestResults> getAllTempbyUserID(Long user_id){
        List<ARTTestResults> toReturn;
        try{
            toReturn = artTestResultRepository.findByUserId(user_id);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
        return toReturn; 
    }

    public ARTTestResults getTempbyUserIDAndDate(Long user_id, LocalDateTime date){
        try{
            return artTestResultRepository.findByUserIdAndDate(user_id,date);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }   

}
