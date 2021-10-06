package src.main.java.com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import java.util.*;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TemperatureService {
    @Autowired
    private TemperatureRepository temperatureRepository;

    public List<Temperature> getAllTemp(){
        return temperatureRepository.findAll();
    }

    public List<Temperature> getAllTempbyUserID(Long user_id){
        return temperatureRepository.findByUserId(user_id); 
    }

    public Temperature getTempbyUserIDAndDate(Long user_id, LocalDateTime date){
        return temperatureRepository.findByUserIdAndDate(user_id,date);
    }

}
