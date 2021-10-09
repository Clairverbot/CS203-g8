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


    public User updateUserVaccinationStatus(User user){
        return userRepository.save(user);

    }

    public User updateUserName(User user){
        return userRepository.save(user);

    }
    
    public User updatepassword(User user) {
        return userRepository.save(user);

    }
    public User updaterole(User user){
        return userRepository.save(user);
    }

    public User updatemanagerid(User user) {
        return userRepository.save(user);
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


//     public int updateUserManagerID(Long ID, Long managerID) {
//         Optional<User> b = userRepository.findById(ID);
//         Optional<User> managerop = userRepository.findById(managerID);
//         if (b.isPresent() && managerop.isPresent()) {
//             User user = b.get();
//             User manager = managerop.get();
//             if(manager.getRole().equals("Admin")){
//                 user.setManager_user(manager);
//                 userRepository.save(user);
//                 return 1;//done perfect
//             }else{
//                 return 10; // bad request bc the supposed inputted manager isnt a manager
//             }
//         } else
//             return 20;//user or manager not found
//     }
 }
