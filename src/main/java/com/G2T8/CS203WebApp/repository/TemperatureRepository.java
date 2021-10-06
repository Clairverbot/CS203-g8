package src.main.java.com.G2T8.CS203WebApp.repository;

import com.G2T8.CS203WebApp.model.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.LocalDateTime;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long>{
    
    @Query(value = "SELECT * FROM temperature t WHERE t.user_id = user_id", nativeQuery= true)
    List<Optional<Temperature>> findByUserId(@Param("user_id") Long user_id);

    @Query(value = "SELECT * FROM temperature t WHERE t.user_id = user_id and t.date =  date", nativeQuery = true)
    Optional<Temperature> findByUserIdAndDate(@Param("user_id") Long user_id, 
            @Param("date") LocalDateTime date);
}

