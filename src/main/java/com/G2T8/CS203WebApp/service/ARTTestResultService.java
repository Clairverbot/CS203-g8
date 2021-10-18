package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import java.time.LocalDateTime;

@Service
public class ARTTestResultService{
    @Autowired
    private ARTTestResultRepository artTestResultRepository;
    @Autowired
    private UserRepository userRepo;

    public List<ARTTestResults> getAllResult(){
        return artTestResultRepository.findAll();
    }

    public List<ARTTestResults> getARTbyUserID(Long user_id){
        Optional<User> user = userRepo.findById(user_id);
        if(!user.isPresent()){
            throw new UserNotFoundException(user_id);
        }
        List<ARTTestResults> toReturn = artTestResultRepository.findByUserId(user_id);
        if(toReturn != null){
            return toReturn;
        }
        return null; 
    }

    public ARTTestResults getARTbyUserIdAndDate(Long user_id, LocalDateTime date){
        Optional<User> user = userRepo.findById(user_id);
        if(!user.isPresent()){
            throw new UserNotFoundException(user_id);
        }
        ARTTestResults toReturn = artTestResultRepository.findByUserIdAndDate(user_id,date);
        if(toReturn != null){
            return toReturn;
        }
        return null;
    }   

    public void addART(ARTTestResults art){
        Optional<User> user = userRepo.findById(art.getUser().getID());
        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        artTestResultRepository.save(art);
    }

}
