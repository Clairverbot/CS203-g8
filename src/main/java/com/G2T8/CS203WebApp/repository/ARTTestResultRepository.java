package src.main.java.com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ARTTestResultRepository extends JpaRepository<ARTTestResult, Long>{
    
}
