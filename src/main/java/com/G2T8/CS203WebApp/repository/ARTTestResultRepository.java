package src.main.java.com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ARTTestResultRepository extends JpaRepository<ARTTestResult, Long>{
    
    @Query(value = "SELECT * FROM arttestresult a WHERE a.user_id = user_id", nativeQuery= true)
    List<Optional<ARTTestResult>> findByUserId(@Param("user_id") Long user_id);

    @Query(value = "SELECT * FROM arttestresut a WHERE a.user_id = user_id and a.date =  date", nativeQuery = true)
    Optional<ARTTestResult> findByUserIdAndDate(@Param("user_id") Long user_id, 
            @Param("date") LocalDateTime date);
}
}
