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

    private TemperatureRepository temperatureRepository;
    private UserService userService;

    @Autowired
    public TemperatureService(TemperatureRepository temperatureRepository, UserService userService) {
        this.temperatureRepository = temperatureRepository;
        this.userService = userService;
    }

    public List<Temperature> getAllTemp() {
        return temperatureRepository.findAll();
    }

    public List<Temperature> getAllTempbyUserID(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        List<Temperature> toReturn = temperatureRepository.findByUser(user);
        return toReturn;
    }

    public Temperature getTempbyUserIDAndDate(Long userId, LocalDateTime date) {
        User user = userService.getUser(userId);
        if (user != null) {
            throw new UserNotFoundException(userId);
        }
        Temperature toReturn = temperatureRepository.findByUserIdAndDate(userId, date);
        return toReturn;
    }

    public Temperature addTemperature(String email, double temperature) {

        CustomUserDetails userObj = (CustomUserDetails) userService.loadUserByUsername(email);
        User userEntity = userObj.getUser();

        if (userEntity == null) {
            throw new UserNotFoundException(email);
        }

        Temperature newTemperature = new Temperature();

        newTemperature.setDate(LocalDateTime.now());
        newTemperature.setUser(userEntity);
        newTemperature.setTemperature(temperature);

        return temperatureRepository.save(newTemperature);
    }

}
