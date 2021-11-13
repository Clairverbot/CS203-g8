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

    /**
     * Get all teams
     * 
     * @return list of all teams
     */
    @GetMapping("/")
    public List<Team> findAllTeams() {
        try {
            return teamService.getAllTeams();
        } catch (Exception E) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurs, please try again!");
        }

    }

    /**
     * Get team based on ID
     * 
     * @param id team ID
     * @return specific team
     */
    @GetMapping("/{id}")
    public Team findTeamById(@PathVariable Long id) {
        return teamService.getTeam(id);
    }

    /**
     * Find users of a certain team
     * 
     * @param id id of team
     * @return set of users in that team
     */
    @GetMapping("/{id}/users")
    public List<User> findUsersByTeamId(@PathVariable Long id) {
        Team team = teamService.getTeam(id);
        return team.getUsers();
    }

    /**
     * Updates the name of a team
     * 
     * @param id   id of the team to update
     * @param name new name
     * @return updated team object
     */
    @PutMapping("/{id}")
    public Team updateName(@PathVariable Long id, @RequestBody String name) {
        return teamService.updateTeamName(id, name);
    }

    /**
     * Creates a new team
     * 
     * @param requestBody key-value pair containing {name: name of team}
     * @return created team, HTTP status code 201 if success
     */
    @PostMapping("/")
    public ResponseEntity<?> addTeam(@RequestBody Map<String, Object> requestBody) {
        Team team = new Team();
        team.setName(requestBody.get("name").toString());
        Team responseBody = teamService.addNewTeam(team);
        return new ResponseEntity<Team>(responseBody, HttpStatus.CREATED);
    }
}
