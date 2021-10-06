package src.main.java.com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ARTTestResultRepository extends JpaRepository<ARTTestResult, Long>{
    
}
