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

    /**
     * Get all covid history
     * 
     * @return list of all covid histories
     */
    @GetMapping("/")
    public List<CovidHistory> findAllCovidHistory() {
        if (covidHistoryService.getAllCovidHistories() != null) {
            return covidHistoryService.getAllCovidHistories();
        }

        throw new CovidHistoryNotFoundException();

    }

    /**
     * Find all covid history of one user
     * 
     * @param id user ID
     * @return list of covid histories associated to one user
     */
    @GetMapping("/find-all-history-from-one-user/{id}")
    public List<CovidHistory> findAllCovidHistoryFromOneUser(@PathVariable Long id) {
        if (covidHistoryService.getAllCovidHistoryFromOneUser(id) != null) {
            return covidHistoryService.getAllCovidHistoryFromOneUser(id);

        }
        throw new CovidHistoryNotFoundException(id);

    }

    /**
     * Find a certain covid history of a certain user by contracted date
     * 
     * @param id             user ID
     * @param contracteddate contracted date
     * @return the specific covid history
     */
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

    /**
     * Create a covid history entry
     * 
     * @param covidHistory covid history details
     * @return covid history created
     */
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

    /**
     * Update covid history recovery date
     * 
     * @param covidHistoryLatest CovidHistoryDTO with updated recovery date
     * @return updated covid history entry
     */
    @PutMapping("/recovery-date")
    public CovidHistory addsRecoveryDate(@Valid @RequestBody CovidHistoryDTO covidHistoryLatest) {
        if (covidHistoryLatest == null || covidHistoryLatest.getUserId() == null
                || covidHistoryLatest.getContractedDate() == null) {

            throw new CovidHistoryNotFoundException();
        }
        return covidHistoryService.updateCovidHistory(covidHistoryLatest);

    }

}
