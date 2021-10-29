package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.G2T8.CS203WebApp.Exception.UserNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
     * Method to get all temperature logs
     * 
     * @return
     */
    public List<Temperature> getAllTemp() {
        return temperatureRepository.findAll();
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

    /**
     * Get all temperature logs of a user with a specific ID
     * 
     * @param userId
     * @return
     */
    public List<Temperature> getAllTempbyUserID(Long userId) {
        User user = validateUser(userId);
        List<Temperature> toReturn = temperatureRepository.findByUser(user);
        return toReturn;
    }

    /**
     * Get all user temperature logs by a certain day using user ID
     * 
     * @param userId
     * @param day
     * @return
     */
    public List<Temperature> getUserTempOnDayByUserId(Long userId, LocalDate day) {
        User user = validateUser(userId);
        return getUserTempOnDay(user, day);
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
        return getUserTempOnDay(user, day);
    }

    /**
     * Utility method to get user temperature logs on a certain day provided a user
     * entity
     * 
     * @param user
     * @param day
     * @return
     */
    private List<Temperature> getUserTempOnDay(User user, LocalDate day) {
        if (user == null) {
            throw new UserNotFoundException("User cannot be found.");
        }
        LocalDateTime lowerBound = day.atStartOfDay();
        LocalDate nextDay = day.plusDays(1);
        LocalDateTime upperBound = nextDay.atStartOfDay();
        return getUserTempBetweenDateTime(user, lowerBound, upperBound);
    }

    /**
     * Utility function to get user temperature logs between two DateTimes
     * 
     * @param user
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public List<Temperature> getUserTempBetweenDateTime(User user, LocalDateTime lowerBound, LocalDateTime upperBound) {
        if (lowerBound.isAfter(upperBound)) {
            throw new IllegalArgumentException("Lower bound for date should be lower than upper bound");
        }
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

}
