package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
public class ARTTestResultService {

    private ARTTestResultRepository artTestResultRepository;
    private UserService userService;

    @Autowired
    public ARTTestResultService(ARTTestResultRepository artTestResultRepository, UserService userService) {
        this.artTestResultRepository = artTestResultRepository;
        this.userService = userService;
    }

    public List<ARTTestResults> getAllResult() {
        return artTestResultRepository.findAll();
    }

    public List<ARTTestResults> getARTbyUserID(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        List<ARTTestResults> toReturn = artTestResultRepository.findByUserId(userId);
        if (toReturn != null) {
            return toReturn;
        }
        return null;
    }

    public ARTTestResults getARTbyUserIdAndDate(Long userId, LocalDateTime date) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        ARTTestResults toReturn = artTestResultRepository.findByUserIdAndDate(userId, date);
        if (toReturn != null) {
            return toReturn;
        }
        return null;
    }

    public ARTTestResults addART(String email, Boolean artResult) {
        CustomUserDetails userObj = (CustomUserDetails) userService.loadUserByUsername(email);
        User userEntity = userObj.getUser();

        if (userEntity == null) {
            throw new UserNotFoundException();
        }

        ARTTestResults art = new ARTTestResults();
        LocalDateTime date = LocalDateTime.now();
        art.setUser(userEntity);
        art.setArtResult(artResult);
        art.setDate(date);
        art.setWeeksMonday(date.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));
        return artTestResultRepository.save(art);
    }

}
