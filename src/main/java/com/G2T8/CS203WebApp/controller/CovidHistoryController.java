package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import javax.mail.MessagingException;
import javax.validation.Valid;

import com.G2T8.CS203WebApp.exception.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/covid-history")
public class CovidHistoryController {

    private final CovidHistoryService covidHistoryService;

    @Autowired
    public CovidHistoryController(CovidHistoryService covidHistoryService) {
        this.covidHistoryService = covidHistoryService;
    }

    // get all CovidHistory

    @GetMapping("/find-all")
    public List<CovidHistory> findAllCovidHistory() {
        if (covidHistoryService.getAllCovidHistories() != null) {
            return covidHistoryService.getAllCovidHistories();
        }

        throw new CovidHistoryNotFoundException();

    }

    @GetMapping("/find-all-history-from-one-user/{id}")
    public List<CovidHistory> findAllCovidHistoryFromOneUser(@PathVariable Long id) {
        if (covidHistoryService.getAllCovidHistoryFromOneUser(id) != null) {
            return covidHistoryService.getAllCovidHistoryFromOneUser(id);

        }
        throw new CovidHistoryNotFoundException(id);

    }

    @GetMapping("/find-one-covid-history-from-one-user/{id}/{contracteddate}")
    public CovidHistory findOneCovidHistoryFromOneUser(@PathVariable(value = "id") Long id,
            @PathVariable(value = "contracteddate") String contracteddate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime contracteddateparsed = LocalDateTime.parse(contracteddate, formatter);
        if (covidHistoryService.getOneCovidHistoryFromOneUser(id, contracteddateparsed) != null) {
            return covidHistoryService.getOneCovidHistoryFromOneUser(id, contracteddateparsed);

        }
        throw new CovidHistoryNotFoundException(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public CovidHistory createCovidHistory(@RequestBody CovidHistory covidHistory) {
        if (covidHistory == null || covidHistory.getUser() == null || covidHistory.getContractedDate() == null) {
            throw new CovidHistoryNotFoundException();
        }

        try {
            return covidHistoryService.addCovidHistory(covidHistory);
        } catch (MessagingException | IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error with emailing has occurred");
        }
    }

    @PutMapping("/recovery-date")
    public CovidHistory addsRecoveryDate(@Valid @RequestBody CovidHistoryDTO covidHistoryLatest) {
        if (covidHistoryLatest == null || covidHistoryLatest.getUserId() == null
                || covidHistoryLatest.getContractedDate() == null) {

            throw new CovidHistoryNotFoundException();
        }
        return covidHistoryService.updateCovidHistory(covidHistoryLatest);

    }

}
