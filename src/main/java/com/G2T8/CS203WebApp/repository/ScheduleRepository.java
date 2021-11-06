package com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.LocalDate;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT * FROM Schedule s WHERE s.teamid = :team_id", nativeQuery = true)
    List<Schedule> findByTeamId(@Param("team_id") Long team_id);

    @Query(value = "SELECT * FROM Schedule s WHERE s.startdate = :startdate and s.enddate = :enddate", nativeQuery = true)
    List<Schedule> findByStartDateAndEndDate(@Param("startdate") LocalDate startdate,
            @Param("enddate") LocalDate enddate);

    @Query(value = "SELECT * FROM Schedule s WHERE s.teamid = :team_id and s.startdate = :startdate", nativeQuery = true)
    Schedule findByTeamIdAndStartDate(@Param("team_id") Long team_id,
            @Param("startdate") LocalDate startdate);
}
