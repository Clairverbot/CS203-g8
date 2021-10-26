package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.G2T8.CS203WebApp.model.*; 
import com.G2T8.CS203WebApp.repository.*; 
import com.G2T8.CS203WebApp.exception.*; 
import java.util.*;
import java.time.LocalDateTime;
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

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getAllScheduleByTeamID(Long teamID) {
        Team team = teamService.getTeam(teamID);
        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }
        List<Schedule> schedules = scheduleRepository.findByTeamId(teamID);
        if (schedules == null) {
            throw new ScheduleNotFoundException(teamID);
        }
        return schedules;
    }

    public Schedule getScheduleByTeamIDAndStartDateTime(Long teamID, LocalDateTime startDateTime) {
        Team team = teamService.getTeam(teamID);
        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }
        Schedule schedule = scheduleRepository.findByTeamIdAndStartDateTime(teamID, startDateTime);
        if (schedule == null) {
            throw new ScheduleNotFoundException(teamID, startDateTime);
        }
        return schedule;
    }

    public Schedule addSchedule(Long teamID, LocalDateTime startDateTime, LocalDateTime endDateTime, int mode) {
        Team team = teamService.getTeam(teamID);
        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }
        // check for intermingling
        if (mode == 1) {
            List<Schedule> schedulesWithSameTime = scheduleRepository.findByStartDateTimeAndEndDateTime(startDateTime, endDateTime);
            if (schedulesWithSameTime != null) {
                throw new ScheduleClashException();
            }
        }
        // 
        Schedule schedule = new Schedule();
        schedule.setTeam(team);
        schedule.setStartDateTime(startDateTime);
        schedule.setEndDateTime(endDateTime);
        schedule.setMode(mode);
        return scheduleRepository.save(schedule);
    }

    public Schedule updateScheduleMode(Long teamID, LocalDateTime startDateTime, int newMode) {
        Team team = teamService.getTeam(teamID);
        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }
        Schedule schedule = scheduleRepository.findByTeamIdAndStartDateTime(teamID, startDateTime);
        if (schedule == null) {
            throw new ScheduleNotFoundException(teamID, startDateTime);
        }

        // check for intermingling
        if (newMode == 1) {
            List<Schedule> schedulesWithSameTime = scheduleRepository.findByStartDateTimeAndEndDateTime(startDateTime, schedule.getEndDateTime());
            if (schedulesWithSameTime != null) {
                throw new ScheduleClashException();
            }
        }
        schedule.setMode(newMode);
        return scheduleRepository.save(schedule);
    }

    public Schedule updateScheduleStartDateTime(Long teamID, LocalDateTime startDateTime,  LocalDateTime newStartDateTime) {
        Team team = teamService.getTeam(teamID);
        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }
        Schedule schedule = scheduleRepository.findByTeamIdAndStartDateTime(teamID, startDateTime);
        if (schedule == null) {
            throw new ScheduleNotFoundException(teamID, startDateTime);
        }
        schedule.setStartDateTime(newStartDateTime);
        return scheduleRepository.save(schedule);
    }


    public Schedule updateScheduleEndDateTime(Long teamID, LocalDateTime startDateTime, LocalDateTime newEndDateTime) {
        Team team = teamService.getTeam(teamID);
        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }
        Schedule schedule = scheduleRepository.findByTeamIdAndStartDateTime(teamID, startDateTime);
        if (schedule == null) {
            throw new ScheduleNotFoundException(teamID, startDateTime);
        }
        schedule.setStartDateTime(newEndDateTime);
        return scheduleRepository.save(schedule);
    }
}
