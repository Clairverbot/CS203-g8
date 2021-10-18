package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.G2T8.CS203WebApp.model.*; 
import java.util.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // returns list because one person can have many schedules
    public List<Schedule> getAllScheduleFromOneUser(Long ID) {

        // getting the schedule of each user by custom query findByUserId in
        // ScheduleRepository
        // do not use findById bc that uses id of schedule bc we cannot use it to 
        // point to the user we want

        List<Optional<Schedule>> origList = scheduleRepository.findByUserId(ID);
        List<Schedule> toReturn = new ArrayList<>();

        if (origList != null) {
            for (int i = 0; i <= origList.size() - 1; i++) {
                if (origList.get(i).isPresent()) {
                    Optional<Schedule> opSchedule = origList.get(i);
                    Schedule sched = opSchedule.get();
                    toReturn.add(sched);

                } else {
                    continue;
                }

            }
            return toReturn;

        }
        return null;

    }

    // returns a particular covidHistory record of one user
    public Schedule getOneScheduleFromOneUser(Long ID, LocalDateTime startdatetime) {
        Optional <Schedule> temp = scheduleRepository.findByUserIdAndStartDateTime(ID, startdatetime);
        if(temp.isPresent()){
            Schedule toReturn = temp.get();
            return toReturn; 

        }
        return null;

    }

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }


    public Schedule updateScheduleMode(Long ID, LocalDateTime startdatetime, int newMode) {
        Optional<Schedule> b = scheduleRepository.findByUserIdAndStartDateTime(ID, startdatetime);
        if (b.isPresent()) {
            Schedule schedule = b.get();
            schedule.setMode(newMode);
            return scheduleRepository.save(schedule);
        } else
            return null;
    }

    public Schedule updateScheduleStartDateTime(Long ID, LocalDateTime startdatetime,  LocalDateTime newStartDateTime) {
        Optional<Schedule> b = scheduleRepository.findByUserIdAndStartDateTime(ID, startdatetime);
        if (b.isPresent()) {
            Schedule schedule = b.get();
            schedule.setStartDateTime(newStartDateTime);
            return scheduleRepository.save(schedule);
        } else
            return null;
    }

    public Schedule updateScheduleEndDateTime(Long ID, LocalDateTime startdatetime, LocalDateTime newEndDateTime) {
        Optional<Schedule> b = scheduleRepository.findByUserIdAndStartDateTime(ID, startdatetime);
        if (b.isPresent()) {
            Schedule schedule = b.get();
            schedule.setEndDateTime(newEndDateTime);
            return scheduleRepository.save(schedule);
        } else
            return null;
    }
    
}
