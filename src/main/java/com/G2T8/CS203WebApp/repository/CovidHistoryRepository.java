package com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.LocalDateTime;

@Repository
public interface CovidHistoryRepository extends JpaRepository<CovidHistory, Long>{

    @Query(value = "SELECT * FROM covid_history c WHERE c.userid = :user_id", nativeQuery= true)
    List<CovidHistory> findByUserId(@Param("user_id") Long user_id);

    @Query(value = "SELECT c FROM covid_history c WHERE c.userid = user_id and c.contracted_date = contracteddate", nativeQuery = true)
    Optional<CovidHistory> findByUserIdAndContractedDate(@Param("user_id") Long user_id, 
            @Param("contracteddate") LocalDateTime contracteddate);


}
    

