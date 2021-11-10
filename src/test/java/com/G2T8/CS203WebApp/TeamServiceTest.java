package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.G2T8.CS203WebApp.model.Team;
import com.G2T8.CS203WebApp.repository.TeamRepository;
import com.G2T8.CS203WebApp.service.TeamService;
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    /**
     * Clear database after every test
     */
    @AfterEach
    void tearDown() {
        teamRepository.deleteAll();
    }

    @Test
    public void getAllTeams_ReturnAllTeams() {
        // arrange
        Team team1 = new Team();
        Team team2 = new Team();

        List<Team> teamList = new ArrayList<Team>();

        teamList.add(team1);
        teamList.add(team2);

        when(teamRepository.findAll()).thenReturn(teamList);

        // act
        List<Team> allTeam = teamService.getAllTeams();

        // assert
        assertEquals(teamList, allTeam);
        verify(teamRepository).findAll();
    }

    @Test
    public void addNewTeam_NewTeam_ReturnNewTeam() {
        // arrange
        Team team = new Team();
        team.setName("team two");

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // act
        Team newTeam = teamService.addNewTeam(team);

        // assert
        assertNotNull(newTeam);
        verify(teamRepository).save(team);
    }

    @Test
    public void getTeam_ReturnTeam() {
        // arrange
        Team team = new Team();
        team.setTeamID(1L);
        Long teamId = team.getTeamID();

        Optional<Team> opt = Optional.of(team);

        when(teamRepository.findById(teamId)).thenReturn(opt);

        // act
        Team toReturn = teamService.getTeam(teamId);

        // assert
        assertEquals(team, toReturn);
        verify(teamRepository).findById(teamId);

    }
}
