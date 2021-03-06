package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.exception.*;
import java.util.*;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
    private TeamService teamService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, TeamService teamService) {
        this.scheduleRepository = scheduleRepository;
        this.teamService = teamService;
    }

    /**
     * Get all schedules
     * 
     * @return list of all schedules
     */
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    /**
     * Get all schedules associated with a team
     * 
     * @param teamID ID of team
     * @return list of all schedules associated with a team
     */
    public List<Schedule> getAllScheduleByTeamID(Long teamID) {
        Team team = findTeam(teamID);
        List<Schedule> schedules = scheduleRepository.findByTeamId(teamID);
        if (schedules == null) {
            throw new ScheduleNotFoundException(teamID);
        }
        return schedules;
    }

    /**
     * Get schedule by team ID and start date
     * 
     * @param teamID    ID of team
     * @param startDate start date of schedule
     * @return specific schedule
     */
    public Schedule getScheduleByTeamIDAndStartDate(Long teamID, LocalDate startDate) {
        Team team = findTeam(teamID);
        Schedule schedule = scheduleRepository.findByTeamIdAndStartDate(teamID, startDate);
        if (schedule == null) {
            throw new ScheduleNotFoundException(teamID, startDate);
        }
        return schedule;
    }

    /**
     * Add schedule
     * 
     * @param teamID    ID of team
     * @param startDate start date of schedule
     * @param endDate   end date of schedule
     * @param mode      mode of work (WFH or Office)
     * @return added schedule
     */
    public Schedule addSchedule(Long teamID, LocalDate startDate, LocalDate endDate, int mode) {
        Team team = findTeam(teamID);

        if (checkScheduleConflict(teamID, mode, startDate, endDate)) {
            throw new ScheduleClashException();
        }

        Schedule schedule = new Schedule();
        schedule.setTeam(team);
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);
        schedule.setMode(mode);
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long scheduleId, Long teamID, LocalDate startDate, LocalDate endDate, int mode) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (!schedule.isPresent()) {
            throw new ScheduleNotFoundException(scheduleId, startDate);
        }
        Team team = findTeam(teamID);

        if (checkScheduleConflict(teamID, mode, startDate, endDate)) {
            throw new ScheduleClashException();
        }

        Schedule updatedSchedule = schedule.get();
        updatedSchedule.setMode(mode);
        updatedSchedule.setTeam(team);
        updatedSchedule.setStartDate(startDate);
        updatedSchedule.setEndDate(endDate);
        return scheduleRepository.save(updatedSchedule);
    }

    /**
     * Delete schedule
     * 
     * @param scheduleId schedule to be deleted
     */
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    /**
     * Find team by ID
     * 
     * @param teamID find team by ID
     * @return team
     */
    public Team findTeam(Long teamID) {
        Team team = teamService.getTeam(teamID);
        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }
        return team;
    }

    /**
     * Checks for schedule conflicts
     * 
     * @param teamID    ID of team
     * @param mode      mode of work
     * @param startDate start date
     * @param endDate   end date
     * @return true if has conflicts, false if not
     */
    public boolean checkScheduleConflict(Long teamID, int mode, LocalDate startDate, LocalDate endDate) {
        List<Schedule> conflicts;
        if (mode == 1) {
            conflicts = scheduleRepository.findAllByTeamIdOrModeAndStartDateBetweenOrEndDateBetween(teamID, mode,
                    startDate, endDate);
        } else {
            conflicts = scheduleRepository.findAllByTeamIdAndStartDateBetweenOrEndDateBetween(teamID, startDate,
                    endDate);
        }

        if (!conflicts.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
