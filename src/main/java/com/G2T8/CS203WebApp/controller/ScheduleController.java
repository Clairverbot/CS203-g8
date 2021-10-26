package com.G2T8.CS203WebApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.G2T8.CS203WebApp.exception.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;
    
    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // get all schedules
    @GetMapping("/")
    public List<Schedule> findAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        if (schedules == null) {
            throw new ScheduleNotFoundException();
        }
        return schedules;
    }

    // get all schedules based on teamID
    @RequestMapping("/{teamID}")
    public List<Schedule> findAllScheduleByTeamID(@PathVariable Long teamID) {
        List<Schedule> schedules;
        try {
            schedules = scheduleService.getAllScheduleByTeamID(teamID);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
        return schedules;
    }

    // get schedule based on teamID and startDateTime
    @GetMapping("/{teamID}/{startDateTime}")
    public Schedule findScheduleByTeamIDAndStartDateTime(@PathVariable Long teamID, @PathVariable LocalDateTime startDateTime) {
        Schedule schedule;
        try {
            schedule = scheduleService.getScheduleByTeamIDAndStartDateTime(teamID, startDateTime);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
        return schedule;
    }

    // update mode
    @PutMapping("/{teamID}/{startDateTime}/{newMode}")
    public void updateMode(@PathVariable Long teamID, @PathVariable LocalDateTime startDateTime, @PathVariable int newMode){
        try{
            scheduleService.updateScheduleMode(teamID, startDateTime, newMode);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
    }

    // update startDateTime
    @PutMapping("/{teamID}/{startDateTime}/{newStartDateTime}")
    public void updateStartDateTime(@PathVariable Long teamID, @PathVariable LocalDateTime startDateTime, @PathVariable LocalDateTime newStartDateTime){
        try{
            scheduleService.updateScheduleStartDateTime(teamID, startDateTime, newStartDateTime);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
    }

    // update endDateTime
    @PutMapping("/{teamID}/{startDateTime}/{newEndDateTime}")
    public void updateEndDateTime(@PathVariable Long teamID, @PathVariable LocalDateTime startDateTime, @PathVariable LocalDateTime newEndDateTime){
        try{
            scheduleService.updateScheduleEndDateTime(teamID, startDateTime, newEndDateTime);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
    }

    // add schedule
    @PostMapping("/")
    public ResponseEntity<?> addSchedule(@RequestParam Long teamID, @RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime, @RequestParam int mode) {
        Schedule schedule;
        try {
            schedule = scheduleService.addSchedule(teamID, startDateTime, endDateTime, mode);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
        return new ResponseEntity<Schedule>(schedule, HttpStatus.CREATED);
    }
}
