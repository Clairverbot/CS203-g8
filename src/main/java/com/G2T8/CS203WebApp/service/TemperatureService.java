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
     * @param userId
     * @return
     */
    public List<Temperature> getAllTempbyUserID(Long userId) {
        User user = validateUser(userId);
        return temperatureRepository.findByUser(user);
    }

    /**
     * Get all user temperature logs by a certain day using email
     * 
     * @param email
     * @param day
     * @return
     */
    public List<Temperature> getUserTempOnDayByUserEmail(String email, LocalDate day) {
        User user = validateUserEmail(email);

        LocalDateTime lowerBound = day.atStartOfDay();
        LocalDate nextDay = day.plusDays(1);
        LocalDateTime upperBound = nextDay.atStartOfDay();

        return temperatureRepository.findAllByUserAndDateBetween(user, lowerBound, upperBound);
    }

    /**
     * Method to add temperature log
     * 
     * @param email
     * @param temperature
     * @return
     */
    public Temperature addTemperature(String email, double temperature) {
        User userEntity = validateUserEmail(email);

        Temperature newTemperature = new Temperature();

        newTemperature.setDate(LocalDateTime.now());
        newTemperature.setUser(userEntity);
        newTemperature.setTemperature(temperature);

        return temperatureRepository.save(newTemperature);
    }

    /**
     * Utility method to validate that a user with the user ID exists
     * 
     * @param userId
     * @return
     */
    private User validateUser(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    /**
     * Utility method to validate that a user with the email exists
     * 
     * @param email
     * @return
     */
    private User validateUserEmail(String email) {
        CustomUserDetails userObj = (CustomUserDetails) userService.loadUserByUsername(email);
        User userEntity = userObj.getUser();

        if (userEntity == null) {
            throw new UserNotFoundException(email);
        }
        return userEntity;
    }
}
