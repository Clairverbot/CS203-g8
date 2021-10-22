package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ResponseStatusException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import org.slf4j.*;

@Service
public class ARTTestResultService {

    private ARTTestResultRepository artTestResultRepository;
    private UserService userService;
    private EmailService emailService;

    Logger logger = LoggerFactory.getLogger(ARTTestResultService.class);

    @Autowired
    public ARTTestResultService(ARTTestResultRepository artTestResultRepository, UserService userService,
            EmailService emailService) {
        this.artTestResultRepository = artTestResultRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public List<ARTTestResults> getAllResult() {
        return artTestResultRepository.findAll();
    }

    public List<ARTTestResults> getARTbyUserID(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        List<ARTTestResults> toReturn = artTestResultRepository.findByUser(user);
        return toReturn;
    }

    public ARTTestResults getARTbyUserIdAndDate(Long userId, LocalDateTime date) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        ARTTestResults toReturn = artTestResultRepository.findByUserIdAndDate(userId, date);
        return toReturn;
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
        art.setWeeksMonday(date.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));
        return artTestResultRepository.save(art);
    }

    public List<ARTTestResults> getARTbyUserAndWeek(User user, LocalDateTime date) {
        LocalDate monday = date.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        return artTestResultRepository.findByUserAndWeeksMonday(user, monday);
    }

    @Scheduled(cron = "0 0 0 * * FRI")
    public void notifyUsersNotYetArtTwice() {
        List<User> users = userService.getAllUsers();
        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            List<ARTTestResults> thisWeekART = getARTbyUserAndWeek(user, now);
            if (thisWeekART.size() < 2) {
                try {
                    Map<String, Object> templateModel = new HashMap<>();
                    templateModel.put("recipientName", user.getName());
                    emailService.sendEmailWithTemplate(user.getEmail(),
                            "[XXX Employee Management System] Submit your ART result!", "art-notification.html",
                            templateModel);
                } catch (Exception e) {
                    logger.error("Email failed to be sent", e);
                    continue;
                }
            }
        }
    }

}
