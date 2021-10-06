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

    public User getUserByEmail(String email){
        Optional<User> b = userRepository.findByEmail(email);
        if (b.isPresent()) {
            User user = b.get();
            return user; 
        } else
            return null;
        
        
    }

    //should delete as we logging in w email not possible to change

    public User updateUserEmail(Long ID, String email){
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            user.setEmail(email);
            return userRepository.save(user);
        } else
            return null;

    }

    public int updateUserVaccinationStatus(Long ID, int vaccstatus) {
        Optional<User> b = userRepository.findById(ID);
        if (b.isPresent()) {
            User user = b.get();
            if( vaccstatus == 1 || vaccstatus == 0 || vaccstatus == 2){
                user.setVaccinationStatus(vaccstatus);
                userRepository.save(user);
                return vaccstatus;
            }else{
                return 10; //new vac status wrong
            }
        } else
            return 20;//not present

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

    public User updateUserPassword(Long ID, String newName) {
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

    public int updateUserRole(Long ID, String role) {
        
        if(role.equals("Admin") || role.equals("Basic")){
            Optional<User> b = userRepository.findById(ID);
            if (b.isPresent()) {
                User user = b.get();
                user.setRole(role);
                userRepository.save(user);
                return 1; // done perfect
            }else{
                return 20; // user not found
            } 
        }
        return 10; // user input wrong role
    }

    public int updateUserManagerID(Long ID, Long managerID) {
        Optional<User> b = userRepository.findById(ID);
        Optional<User> managerop = userRepository.findById(managerID);
        if (b.isPresent() && managerop.isPresent()) {
            User user = b.get();
            User manager = managerop.get();
            if(manager.getRole().equals("Admin")){
                user.setManagerUser(manager);
                userRepository.save(user);
                return 1;//done perfect
            }else{
                return 10; // bad request bc the supposed inputted manager isnt a manager
            }
        } else
            return 20;//user or manager not found
    }

    
}
