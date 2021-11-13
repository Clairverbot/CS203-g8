package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T8.CS203WebApp.exception.UserNotFoundException;

import java.time.LocalDate;
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

    /**
     * Get all temperature logs of a user with a specific ID
     * 
     * @param userId ID of user
     * @return list of temperatures associated with user
     */
    public List<Temperature> getAllTempbyUserID(Long userId) {
        User user = userService.getUser(userId);
        return temperatureRepository.findByUser(user);
    }

    /**
     * Get all user temperature logs by a certain day using email
     * 
     * @param email email of user
     * @param day   date of day to search
     * @return list of temperature logs
     */
    public List<Temperature> getUserTempOnDayByUserEmail(String email, LocalDate day) {
        User user = userService.findByEmail(email);

        LocalDateTime lowerBound = day.atStartOfDay();
        LocalDate nextDay = day.plusDays(1);
        LocalDateTime upperBound = nextDay.atStartOfDay();

        return temperatureRepository.findAllByUserAndDateBetween(user, lowerBound, upperBound);
    }

    /**
     * Method to add temperature log
     * 
     * @param email       email of user
     * @param temperature temperature value
     * @return added temperature log
     */
    public Temperature addTemperature(String email, double temperature) {
        User userEntity = userService.findByEmail(email);

        Temperature newTemperature = new Temperature();

        newTemperature.setDate(LocalDateTime.now());
        newTemperature.setUser(userEntity);
        newTemperature.setTemperature(temperature);

        return temperatureRepository.save(newTemperature);
    }

}
