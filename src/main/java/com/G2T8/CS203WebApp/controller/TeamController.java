package com.G2T8.CS203WebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.G2T8.CS203WebApp.exception.TeamNotFoundException;
import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.service.TeamService;

import org.slf4j.*;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private TeamService teamService;

    Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Get all teams
    @GetMapping("/")
    public List<Team> findAllTeams() {
        try {
            return teamService.getAllTeams();
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }

    }

    // Get team based on id
    @GetMapping("/{id}")
    public Team findTeamById(@PathVariable Long id) {
        Team team;
        try {
            team = teamService.getTeam(id);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
        return team;
    }

    /**
     * Find users of a certain team
     * 
     * @param id id of team
     * @return set of users in that team
     */
    @GetMapping("/{id}/users")
    public List<User> findUsersByTeamId(@PathVariable Long id) {
        Team team;
        try {
            team = teamService.getTeam(id);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }

        return team.getUsers();

    }

    // Update team name based on id
    @PutMapping("/{id}")
    public void updateName(@PathVariable Long id, @RequestBody String name) {
        try {
            teamService.updateTeamName(id, name);
        } catch (TeamNotFoundException E) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team doesn't exist");
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }

    // Add team
    @PostMapping("/")
    public ResponseEntity<?> addTeam(@RequestBody Team team) {
        try {
            teamService.addNewTeam(team);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }
    }
}
