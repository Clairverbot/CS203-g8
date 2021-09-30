package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.UserRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long ID){

        return userRepository.findById(ID).map(user -> {
            return user;
        }).orElse(null);
        
    }

    public User updateUserEmail(Long ID, String email){
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            user.setEmail(email);
            return userRepository.save(user);
        } else
            return null;

    }

    public User updateUserVaccinationStatus(Long ID, int vaccstatus) {
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            user.setVaccinationStatus(vaccstatus);
            return userRepository.save(user);
        } else
            return null;

    }

    public User updateUserName(Long ID, String newName) {
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            user.setName(newName);
            return userRepository.save(user);
        } else
            return null;

    }

    // public User updateUserTeamID(Long ID, Long TeamID) {
    //     Optional<User> b = userRepository.findById(ID);
    //     if (b.isPresent()) {
    //         User user = b.get();
    //         user.setteam(TeamID);
    //         return userRepository.save(user);
    //     } else
    //         return null;

    // }

    public User updateUserRole(Long ID, String role) {
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            user.setRole(role);
            return userRepository.save(user);
        } else
            return null;

    }

    public User updateUseManagerID(Long ID, User managerID) {
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            user.setManagerUser(managerID);
            return userRepository.save(user);
        } else
            return null;

    }

    
}
