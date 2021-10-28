package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;

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

    public List<Temperature> getAllTemp() {
        return temperatureRepository.findAll();
    }

    private User validateUser(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    public List<Temperature> getAllTempbyUserID(Long userId) {
        User user = validateUser(userId);
        List<Temperature> toReturn = temperatureRepository.findByUser(user);
        return toReturn;
    }

    public Temperature getTempbyUserIDAndDate(Long userId, LocalDateTime date) {
        User user = validateUser(userId);
        Temperature toReturn = temperatureRepository.findByUserIdAndDate(userId, date);
        return toReturn;
    }

    public List<Temperature> getUserTempOnDay(Long userId, LocalDate day) {
        User user = validateUser(userId);
        LocalDateTime lowerBound = day.atStartOfDay();
        LocalDate nextDay = day.plusDays(1);
        LocalDateTime upperBound = nextDay.atStartOfDay();
        return getUserTempBetweenDateTime(user, lowerBound, upperBound);
    }

    public List<Temperature> getUserTempBetweenDateTime(User user, LocalDateTime lowerBound, LocalDateTime upperBound) {
        return temperatureRepository.findAllByUserAndDateBetween(user, lowerBound, upperBound);
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
