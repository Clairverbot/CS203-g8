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
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT s FROM Schedule s WHERE s.userid = user_id", nativeQuery = true)
    List<Optional<Schedule>> findByUserId(@Param("user_id") Long user_id);

    @Query(value = "SELECT s FROM Schedule s WHERE s.userid = user_id and s.start_date_time = startdatetime", nativeQuery = true)
    Optional<Schedule> findByUserIdAndStartDateTime(@Param("user_id") Long user_id,
            @Param("startdatetime") LocalDateTime startdatetime);
    
}
