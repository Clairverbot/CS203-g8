package com.G2T8.CS203WebApp.repository;

import com.G2T8.CS203WebApp.model.ARTTestResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;
import java.time.LocalDateTime;

@Repository
public interface ARTTestResultRepository extends JpaRepository<ARTTestResults, Long>{
    
    @Query(value = "SELECT * FROM arttest_results a WHERE a.user_id = :user_id", nativeQuery= true)
    List<ARTTestResults> findByUserId(@Param("user_id") Long user_id);

    @Query(value = "SELECT * FROM arttest_results a WHERE a.user_id = :user_id and a.date = :date", nativeQuery = true)
    ARTTestResults findByUserIdAndDate(@Param("user_id") Long user_id, 
            @Param("date") LocalDateTime date);
}
