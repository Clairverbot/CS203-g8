package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;
import javax.mail.MessagingException;
import javax.validation.Valid;

import com.G2T8.CS203WebApp.exception.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/CovidHistory")
public class CovidHistoryController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CovidHistoryService covidHistoryService;
    @Autowired
    private CovidHistoryRepository covidHistoryRepository;

    // get all CovidHistory

    @GetMapping("/findAll")
    public List<CovidHistory> findAllCovidHistory() {
        if (covidHistoryService.getAllCovidHistories() != null) {
            return covidHistoryService.getAllCovidHistories();
        }

        throw new CovidHistoryNotFoundException();

    }

    @GetMapping("/findAllHistoryFromOneUser/{ID}")
    public List<CovidHistory> findAllCovidHistoryFromOneUser(Long ID) {
        if (covidHistoryService.getAllCovidHistoryFromOneUser(ID) != null) {
            return covidHistoryService.getAllCovidHistoryFromOneUser(ID);

        }
        throw new CovidHistoryNotFoundException(ID);

    }

    // doesnt work
    @GetMapping("/findOneCovidHistoryFromOneUser/{ID}/{contracteddate}")
    public CovidHistory findOneCovidHistoryFromOneUser(
            @PathVariable(value = "ID")Long ID, 
            @PathVariable(value = "contracteddate")String contracteddate) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
                LocalDateTime contracteddateparsed = LocalDateTime.parse(contracteddate, formatter);
        if (covidHistoryService.getOneCovidHistoryFromOneUser(ID, contracteddateparsed) != null) {
            return covidHistoryService.getOneCovidHistoryFromOneUser(ID, contracteddateparsed);

        }
        throw new CovidHistoryNotFoundException(ID);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = { MessagingException.class, IOException.class })
    @RequestMapping(value = "/createCovidHistory", method = RequestMethod.POST)
    public CovidHistory createCovidHistory(@RequestBody CovidHistory covidHistory)
            throws MessagingException, IOException {
        if (covidHistory == null || covidHistory.getUser() == null || covidHistory.getContractedDate() == null) {

            throw new CovidHistoryNotFoundException();
        }
        User user = covidHistory.getUser();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", user.getName());
        emailService.sendEmailWithTemplate(user.getEmail(),
                "[XXX Employee Management System] Instructions To Covid Employee!", "covid-notification.html",
                templateModel);

        return covidHistoryRepository.save(covidHistory);
    }


    // doesnt work
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/updateRecoveryDate", method = RequestMethod.POST)
    public CovidHistory addsRecoveryDate(
            @Valid @RequestBody CovidHistory covidHistoryLatest) {
        if (covidHistoryLatest == null || covidHistoryLatest.getUser() == null || covidHistoryLatest.getContractedDate() == null) {

            throw new CovidHistoryNotFoundException();
        }
        return covidHistoryRepository.save(covidHistoryLatest);

    }
    
}
