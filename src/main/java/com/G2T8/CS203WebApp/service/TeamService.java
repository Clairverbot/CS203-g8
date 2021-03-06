package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.TeamRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.G2T8.CS203WebApp.exception.TeamNotFoundException;

@Service
public class TeamService {

    private TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Get list of all teams
     * 
     * @return list of all teams
     */
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Get team by ID
     * 
     * @param ID team ID
     * @return specific team
     */
    public Team getTeam(Long ID) {
        Optional<Team> t = teamRepository.findById(ID);
        if (t.isPresent()) {
            Team team = t.get();
            return team;
        } else {
            throw new TeamNotFoundException(ID);
        }
    }

    /**
     * Update team name
     * 
     * @param ID      team ID
     * @param newName updated team name
     * @return specific team
     */
    public Team updateTeamName(Long ID, String newName) {
        Optional<Team> b = teamRepository.findById(ID);
        if (b.isPresent()) {
            Team team = b.get();
            team.setName(newName);
            return teamRepository.save(team);
        } else {
            throw new TeamNotFoundException(ID);
        }
    }

    /**
     * Add a new team
     * 
     * @param team team entity to add
     * @return added team
     */
    public Team addNewTeam(Team team) {
        return teamRepository.save(team);
    }
}
