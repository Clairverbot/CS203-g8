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

    @Query(value = "SELECT s FROM Schedule s WHERE s.teamid = team_id", nativeQuery = true)
    List<Schedule> findByTeamId(@Param("team_id") Long team_id);

    @Query(value = "SELECT s FROM Schedule s WHERE s.start_date_time = startdatetime and s.end_date_time = enddatetime", nativeQuery = true)
    List<Schedule> findByStartDateTimeAndEndDateTime(@Param("startdatetime") LocalDateTime startdatetime,
            @Param("enddatetime") LocalDateTime enddatetime);

    @Query(value = "SELECT s FROM Schedule s WHERE s.teamid = team_id and s.start_date_time = startdatetime", nativeQuery = true)
    Schedule findByTeamIdAndStartDateTime(@Param("team_id") Long team_id,
            @Param("startdatetime") LocalDateTime startdatetime);
}
