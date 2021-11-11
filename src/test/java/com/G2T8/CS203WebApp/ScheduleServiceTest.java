package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;
import java.beans.Transient;
import java.lang.Long;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.*;
import com.G2T8.CS203WebApp.service.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TeamService teamService;

    @InjectMocks
    private ScheduleService scheduleService;

    private static User adminUser;
    private static User basicUser;
    private static Team team;

    @BeforeAll
    public static void setup() {
        // setup users
        List<User> users = new ArrayList<>();

        adminUser = new User();
        adminUser.setID(Long.valueOf(1));
        adminUser.setRole("ROLE_ADMIN");
        adminUser.setName("Test Admin");
        adminUser.setEmail("test_admin@gmail.com");
        adminUser.setPassword("password");
        adminUser.setFirstLogin(false);
        users.add(adminUser);

        basicUser = new User();
        basicUser.setID(Long.valueOf(2));
        basicUser.setRole("ROLE_BASIC");
        basicUser.setName("Test Basic");
        basicUser.setEmail("test_basic@gmail.com");
        basicUser.setPassword("password");
        basicUser.setFirstLogin(false);
        users.add(basicUser);

        // setup team
        team = new Team();
        team.setTeamID(Long.valueOf(1));
        team.setName("Test Team");
        team.setUsers(users);
    }

    /**
     * Clear database after every test
     */
    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
    }

    @Test
    public void getAllSchedules_ReturnAllSchedules() {
        // arrange
        Schedule schedule1 = new Schedule();
        schedule1.setID(Long.valueOf(1));
        schedule1.setStartDate(LocalDate.of(2021, 11, 1));
        schedule1.setEndDate(LocalDate.of(2021, 11, 5));
        schedule1.setMode(0);
        schedule1.setTeam(team);

        Schedule schedule2 = new Schedule();
        schedule2.setID(Long.valueOf(2));
        schedule2.setStartDate(LocalDate.of(2021, 11, 8));
        schedule2.setEndDate(LocalDate.of(2021, 11, 12));
        schedule2.setMode(1);
        schedule2.setTeam(team);

        List<Schedule> scheduleList = new ArrayList<>();

        scheduleList.add(schedule1);
        scheduleList.add(schedule2);

        when(scheduleRepository.findAll()).thenReturn(scheduleList);

        // act
        List<Schedule> allSchedules = scheduleService.getAllSchedules();

        // assert
        assertNotNull(allSchedules);
        assertEquals(scheduleList, allSchedules);
        verify(scheduleRepository).findAll();
    }

    @Test
    public void addSchedule_NewSchedule_ReturnSchedule() {
        // arrange
        Schedule newSchedule = new Schedule();
        // newSchedule.setID(Long.valueOf(1));
        newSchedule.setStartDate(LocalDate.of(2021, 11, 1));
        newSchedule.setEndDate(LocalDate.of(2021, 11, 5));
        newSchedule.setMode(0);
        newSchedule.setTeam(team);

        when(teamService.getTeam(any(Long.class))).thenReturn(team);
        when(scheduleRepository.findAllByTeamIdAndStartDateBetweenOrEndDateBetween(team.getTeamID(),
                LocalDate.of(2021, 11, 1), LocalDate.of(2021, 11, 5))).thenReturn(new ArrayList<Schedule>());
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(newSchedule);

        // act
        Schedule returnedSchedule = scheduleService.addSchedule(team.getTeamID(), LocalDate.of(2021, 11, 1),
                LocalDate.of(2021, 11, 5), 0);

        // assert
        assertNotNull(returnedSchedule);
        verify(teamService).getTeam(team.getTeamID());
        verify(scheduleRepository).findAllByTeamIdAndStartDateBetweenOrEndDateBetween(team.getTeamID(),
                LocalDate.of(2021, 11, 1), LocalDate.of(2021, 11, 5));
        verify(scheduleRepository).save(newSchedule);
    }
}
