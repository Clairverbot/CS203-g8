package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@Service
public class TemperatureService {
    @Autowired
    private TemperatureRepository temperatureRepository;

    public List<Temperature> getAllTemp(){
        try{
            return temperatureRepository.findAll();
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    public List<Temperature> getAllTempbyUserID(Long user_id){
        List<Temperature> toReturn;
        try{
            toReturn = temperatureRepository.findByUserId(user_id);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
        return toReturn;
    }

    public Temperature getTempbyUserIDAndDate(Long user_id, LocalDateTime date){
        try{
            return temperatureRepository.findByUserIdAndDate(user_id,date);
        } catch(Exception E){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unknown error occurs, please try again!");
        }
    }

    public void addTemperature(TemperatureDTO tempDetails){
        Temperature temp = new Temperature();
        temp.setDate(tempDetails.getDate());
        temp.setTemperature(artDetails.getTemperature());
        TemperatureRepository.save(temp);
    }

}
