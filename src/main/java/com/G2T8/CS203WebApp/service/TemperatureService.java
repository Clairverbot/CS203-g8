package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    public List<Temperature> getAllTemp(){
        return temperatureRepository.findAll();
    }

    public List<Temperature> getAllTempbyUserID(Long user_id){
        List<Temperature> toReturn = temperatureRepository.findByUserId(user_id);
        if(toReturn != null){
            return toReturn;
        }
        return null;
    }

    public Temperature getTempbyUserIDAndDate(Long user_id, LocalDateTime date){
        Temperature toReturn = temperatureRepository.findByUserIdAndDate(user_id,date);
        if(toReturn != null){
            return toReturn;
        }
        return null;
    }

    public void addTemperature(TemperatureDTO tempDetails){
        Optional<User> user = userRepository.findById(tempDetails.getUserId());
        Temperature temp = new Temperature();
        if(user.isPresent()){
            temp.setDate(tempDetails.getDate());
            temp.setTemperature(tempDetails.getTemperature());
            temp.setUser(user.get());
            temperatureRepository.save(temp);
        }
    }

}
