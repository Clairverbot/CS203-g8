package src.main.java.com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service

public class ARTTestResultService {
    @Autowired
    private ARTTestResultRepository artTestResultRepository;

    public List<ARTTestResult> getAllResult(){
        return artTestResultRepository.findAll();
    }

    public List<ARTTestResult> getAllTempbyUserID(Long user_id){
        return artTestResultRepository.findByUserId(user_id); 
    }

    public ARTTestResult getTempbyUserIDAndDate(Long user_id, LocalDateTime date){
        return artTestResultRepository.findByUserIdAndDate(user_id,date);
    }   

}
