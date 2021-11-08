package com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CovidHistoryRepository extends JpaRepository<CovidHistory, Long>{

    List<CovidHistory> findByUserID(Long userid);

}
    

