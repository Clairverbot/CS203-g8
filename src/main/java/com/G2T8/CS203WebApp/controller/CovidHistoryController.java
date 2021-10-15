package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.validation.Valid;

import com.G2T8.CS203WebApp.exception.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*; 
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/CovidHistory")
public class CovidHistoryController {

    @Autowired
    private CovidHistoryService covidHistoryService; 
    @Autowired
    private CovidHistoryRepository covidHistoryRepository;

    // get all CovidHistory 

    @GetMapping("/findAll")
    public List<CovidHistory> findAllCovidHistory(){
        if (covidHistoryService.getAllCovidHistories() != null) {
            return covidHistoryService.getAllCovidHistories();
        }
        
        throw new CovidHistoryNotFoundException();

    }

    @GetMapping("/findAllHistoryFromOneUser/{ID}")
    public List<CovidHistory> findAllCovidHistoryFromOneUser(Long ID){
        if(covidHistoryService.getAllCovidHistoryFromOneUser(ID) != null){
            return covidHistoryService.getAllCovidHistoryFromOneUser(ID); 

        }
        throw new CovidHistoryNotFoundException(ID);

    }
// doesnt work
    @GetMapping("/findOneCovidHistoryFromOneUser/{ID}/{contracteddate}")
    public CovidHistory findOneCovidHistoryFromOneUser(Long ID, LocalDateTime contracteddate){
        if (covidHistoryService.getOneCovidHistoryFromOneUser(ID, contracteddate) != null) {
            return covidHistoryService.getOneCovidHistoryFromOneUser(ID, contracteddate);

        }
        throw new CovidHistoryNotFoundException(ID);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/createCovidHistory", method = RequestMethod.POST)
    public CovidHistory createCovidHistory(@RequestBody CovidHistory covidHistory){
        if(covidHistory == null || covidHistory.getUser() == null ||
         covidHistory.getContractedDate() == null){
            
            throw new CovidHistoryNotFoundException();
        }
        
        return covidHistoryRepository.save(covidHistory);
    }

// doesnt work
    @PutMapping("/addRecoveryDate/{id}/contracteddate = {contracteddate}")
    public CovidHistory addsRecoveryDate(@PathVariable Long id,
            @PathVariable LocalDateTime contracteddate, 
            @Valid @RequestBody CovidHistory covidHistoryLatest){
        Optional<CovidHistory> temp = covidHistoryRepository.findByUserIdAndContractedDate(id, contracteddate);
        if (temp.isPresent()) {
            CovidHistory toReturn = temp.get();
            toReturn = covidHistoryLatest; 
            return covidHistoryRepository.save(toReturn);
        }
        throw new CovidHistoryNotFoundException(id);

    }













    
}
