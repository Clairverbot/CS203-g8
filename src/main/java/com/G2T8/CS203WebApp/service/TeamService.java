package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.model.*;
import com.G2T8.CS203WebApp.repository.TeamRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;


    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeam(Long ID) {

        return teamRepository.findById(ID).map(team -> {
            return team;
        }).orElse(null);

    }

    public Team updateTeamName(Long ID, String newName) {
        Optional<Team> b = teamRepository.findById(ID);
        if (b.isPresent()) {
            Team team = b.get();
            team.setName(newName);
            return teamRepository.save(team);
        } else
            return null;

    }


    
}
