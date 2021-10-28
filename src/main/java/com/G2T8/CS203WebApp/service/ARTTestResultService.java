package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    /**
     * Method to get all ART results
     * 
     * @return
     */
    public List<ARTTestResults> getAllResult() {
        return artTestResultRepository.findAll();
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
     * Get all ART test results of a certain user by user ID
     * 
     * @param userId
     * @return
     */
    public List<ARTTestResults> getARTbyUserID(Long userId) {
        User user = validateUser(userId);
        List<ARTTestResults> toReturn = artTestResultRepository.findByUser(user);
        return toReturn;
    }

    /**
     * Get a certain ART test result of a user by specific user ID and dateTime
     * 
     * @param userId
     * @param date
     * @return
     */
    public ARTTestResults getARTbyUserIdAndDate(Long userId, LocalDateTime date) {
        User user = validateUser(userId);
        ARTTestResults toReturn = artTestResultRepository.findByUserIdAndDate(userId, date);
        return toReturn;
    }

    /**
     * Method to add ART test
     * 
     * @param email
     * @param artResult
     * @return
     */
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

    /**
     * Method to get all ART test results posted by a user on a certain week by user
     * ID
     * 
     * @param userId
     * @param date
     * @return
     */
    public List<ARTTestResults> getARTbyUserIdAndWeek(Long userId, LocalDateTime date) {
        User user = validateUser(userId);
        return getARTbyUserAndWeek(user, date);
    }

    /**
     * Method to get all ART test results posted by a user on a certain week by user
     * email
     * 
     * @param email
     * @param date
     * @return
     */
    public List<ARTTestResults> getARTbyUserEmailAndWeek(String email, LocalDateTime date) {
        User user = validateUserEmail(email);
        return getARTbyUserAndWeek(user, date);
    }

    /**
     * Utility method to get ART test results of a user on a certain week
     * corresponding to the given date
     * 
     * @param user
     * @param date
     * @return
     */
    private List<ARTTestResults> getARTbyUserAndWeek(User user, LocalDateTime date) {
        LocalDate monday = date.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        return artTestResultRepository.findByUserAndWeeksMonday(user, monday);
    }

    /**
     * Method to notify users that have not done their ART tests twice for that week
     * Scheduled to run every 12 AM on Friday (final working day of the week)
     */
    @Scheduled(cron = "0 0 0 * * FRI")
    public void notifyUsersNotYetArtTwice() {
        List<User> users = userService.getAllUsers();
        LocalDateTime now = LocalDateTime.now();

        // Loops through all users and send them an email if they only have 0-1 ART test
        // result records for that week
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
