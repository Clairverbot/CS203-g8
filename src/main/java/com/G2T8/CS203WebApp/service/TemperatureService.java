package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import java.time.LocalDateTime;

@Service
public class TemperatureService {
    @Autowired
    private TemperatureRepository temperatureRepository;
    @Autowired
    private UserRepository userRepo;

    public List<Temperature> getAllTemp(){
        return temperatureRepository.findAll();
    }

    public List<Temperature> getAllTempbyUserID(Long user_id){
        Optional<User> user = userRepo.findById(user_id);
        if(!user.isPresent()){
            throw new UserNotFoundException(user_id);
        }
        List<Temperature> toReturn = temperatureRepository.findByUserId(user_id);
        if(toReturn != null){
            return toReturn;
        }
        return null;
    }

    public Temperature getTempbyUserIDAndDate(Long user_id, LocalDateTime date){
        Optional<User> user = userRepo.findById(user_id);
        if(!user.isPresent()){
            throw new UserNotFoundException(user_id);
        }
        Temperature toReturn = temperatureRepository.findByUserIdAndDate(user_id,date);
        if(toReturn != null){
            return toReturn;
        }
        return null;
    }

    public void addTemperature(Temperature temperature){
        Optional<User> user = userRepo.findById(temperature.getUser().getID());
        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        temperatureRepository.save(temperature);
    }

}
