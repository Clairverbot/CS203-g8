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

    /**
     * Find all schedules
     * 
     * @return list of all schedules
     */
    @GetMapping("/")
    public List<Schedule> findAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        if (schedules == null) {
            throw new ScheduleNotFoundException();
        }
        return schedules;
    }

    /**
     * Get list of all schedules of a certain team (by team ID)
     * 
     * @param teamID id of team you want to query the schedules for
     * @return list of schedules associated with the team
     */
    @GetMapping("/{teamID}")
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

    /**
     * Get a specific schedule based on team ID and start date
     * 
     * @param teamID    id of team
     * @param startDate start date
     * @return specific schedule
     */
    @GetMapping("/{teamID}/{startDate}")
    public Schedule findScheduleByTeamIDAndStartDate(@PathVariable Long teamID, @PathVariable LocalDate startDate) {
        Schedule schedule;
        try {
            schedule = scheduleService.getScheduleByTeamIDAndStartDate(teamID, startDate);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
        return schedule;
    }

    /**
     * Updates a schedule's information
     * 
     * @param scheduleDTO schedule DTO object to pass in information about schedule
     * @return updated schedule, HTTP status 200 if success
     */
    @PutMapping("/")
    public ResponseEntity<?> updateSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule;
        try {
            schedule = scheduleService.updateSchedule(scheduleDTO.getScheduleId(), scheduleDTO.getTeamId(),
                    scheduleDTO.getStartDate(), scheduleDTO.getEndDate(), scheduleDTO.getMode());
        } catch (TeamNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (ScheduleClashException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule conflict");

        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
        return new ResponseEntity<Schedule>(schedule, HttpStatus.OK);
    }

    /**
     * Creates a new schedule
     * 
     * @param scheduleDTO schedule DTO object to pass in information about schedule
     * @return added schedule, HTTP status of 201 if success
     */
    @PostMapping("/")
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule;
        try {
            schedule = scheduleService.addSchedule(scheduleDTO.getTeamId(), scheduleDTO.getStartDate(),
                    scheduleDTO.getEndDate(), scheduleDTO.getMode());
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Team");
        } catch (ScheduleNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Schedule");
        } catch (ScheduleClashException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule conflict");

        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occured");
        }
        return new ResponseEntity<Schedule>(schedule, HttpStatus.CREATED);
    }

    /**
     * Deletes a schedule by specified ID
     * 
     * @param scheduleId id of schedule
     * @return HTTP status code 200 if success
     */
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
