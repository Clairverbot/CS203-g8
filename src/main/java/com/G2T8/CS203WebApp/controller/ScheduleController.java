package com.G2T8.CS203WebApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.G2T8.CS203WebApp.exception.*;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.*;

import java.time.LocalDate;
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
        return scheduleService.getAllSchedules();
    }

    // get all schedules based on teamID
    @GetMapping("/{teamID}")
    public List<Schedule> findAllScheduleByTeamID(@PathVariable Long teamID) {
        return scheduleService.getAllScheduleByTeamID(teamID);

    }

    // get schedule based on teamID and startDate
    @GetMapping("/{teamID}/{startDate}")
    public Schedule findScheduleByTeamIDAndStartDate(@PathVariable Long teamID, @PathVariable LocalDate startDate) {
        return scheduleService.getScheduleByTeamIDAndStartDate(teamID, startDate);

    }

    @PutMapping("/")
    public Schedule updateSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return scheduleService.updateSchedule(scheduleDTO.getScheduleId(), scheduleDTO.getTeamId(),
                scheduleDTO.getStartDate(), scheduleDTO.getEndDate(), scheduleDTO.getMode());

    }
    // // update mode
    // @PutMapping("/{teamID}/{startDate}/{newMode}")
    // public void updateMode(@PathVariable Long teamID, @PathVariable LocalDate
    // startDate, @PathVariable int newMode) {
    // try {
    // scheduleService.updateScheduleMode(teamID, startDate, newMode);
    // } catch (TeamNotFoundException E) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find
    // Team");
    // } catch (ScheduleNotFoundException E) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find
    // Schedule");
    // } catch (Exception E) {
    // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown
    // error occured");
    // }
    // }

    // // update startDate
    // @PutMapping("/{teamID}/{startDate}/{newStartDate}")
    // public void updateStartDate(@PathVariable Long teamID, @PathVariable
    // LocalDate startDate,
    // @PathVariable LocalDate newStartDate) {
    // try {
    // scheduleService.updateScheduleStartDate(teamID, startDate, newStartDate);
    // } catch (TeamNotFoundException E) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find
    // Team");
    // } catch (ScheduleNotFoundException E) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find
    // Schedule");
    // } catch (Exception E) {
    // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown
    // error occured");
    // }
    // }

    // // update endDate
    // @PutMapping("/{teamID}/{startDate}/{newEndDate}")
    // public void updateEndDate(@PathVariable Long teamID, @PathVariable LocalDate
    // startDate,
    // @PathVariable LocalDate newEndDate) {
    // try {
    // scheduleService.updateScheduleEndDate(teamID, startDate, newEndDate);
    // } catch (TeamNotFoundException E) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find
    // Team");
    // } catch (ScheduleNotFoundException E) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find
    // Schedule");
    // } catch (Exception E) {
    // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown
    // error occured");
    // }
    // }

    // add schedule
    @PostMapping("/")
    public Schedule addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return scheduleService.addSchedule(scheduleDTO.getTeamId(), scheduleDTO.getStartDate(),
                scheduleDTO.getEndDate(), scheduleDTO.getMode());

    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        try {
            scheduleService.deleteSchedule(scheduleId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        }
        return ResponseEntity.ok().build();

    }

}
