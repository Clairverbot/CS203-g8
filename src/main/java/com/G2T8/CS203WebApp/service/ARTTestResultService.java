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
     * @return list of all ART results in database
     */
    public List<ARTTestResult> getAllResult() {
        return artTestResultRepository.findAll();
    }

    /**
     * Utility method to validate that a user with the user ID exists
     * 
     * @param userId of user to find
     * @return validated User with the user ID userId
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
     * @param email of user to be validated
     * @return User that has been validated
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
     * @param userId of user to get all ART test results of
     * @return List of art results from user with user ID userId
     */
    public List<ARTTestResult> getARTbyUserID(Long userId) {
        User user = validateUser(userId);
        List<ARTTestResult> toReturn = artTestResultRepository.findByUser(user);
        return toReturn;
    }

    /**
     * Method to add ART test result
     * 
     * @param email of user that is adding the ART test result
     * @param artResult to be added to the db
     * @return the newly created ARTTestResult object
     */
    public ARTTestResult addART(String email, Boolean artResult) {
        CustomUserDetails userObj = (CustomUserDetails) userService.loadUserByUsername(email);
        User userEntity = userObj.getUser();

        if (userEntity == null) {
            throw new UserNotFoundException();
        }

        // get current DateTime
        LocalDateTime date = LocalDateTime.now();

        // create new ART Test Result
        ARTTestResult newArtTestResult = new ARTTestResult(userEntity, artResult, date, date.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        return artTestResultRepository.save(newArtTestResult);
    }

    /**
     * Method to get all ART test results posted by a user on a 
     * certain week by user email
     * 
     * @param email of user to get the ART test results of
     * @param date find all ART test results submitted by user during 
     * the week this date is in
     * @return List of ARTTestResults of user with email email 
     * submitted on the week of date
     */
    public List<ARTTestResult> getARTbyUserEmailAndWeek(String email, LocalDateTime date) {
        User user = validateUserEmail(email);
        return getARTbyUserAndWeek(user, date);
    }

    /**
     * Utility method to get ART test results of a user on a certain week
     * corresponding to the given date
     * 
     * @param user to get the ART test results of
     * @param date find all ART test results submitted by user during 
     * the week this date is in
     * @return List of ARTTestResults of user user submitted on the week 
     * of date
     */
    private List<ARTTestResult> getARTbyUserAndWeek(User user, LocalDateTime date) {
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
            List<ARTTestResult> thisWeekART = getARTbyUserAndWeek(user, now);
            if (thisWeekART.size() < 2) {
                try {
                    Map<String, Object> templateModel = new HashMap<>();
                    templateModel.put("recipientName", user.getName());
                    emailService.sendEmailWithTemplate(user.getEmail(),
                            "[GR8 Employee Management System] Submit your ART result!", "art-notification.html",
                            templateModel);
                } catch (Exception e) {
                    logger.error("Email failed to be sent", e);
                    continue;
                }
            }
        }
    }

}
