package com.G2T8.CS203WebApp.repository;

import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

        @Query(value = "SELECT * FROM Schedule s WHERE s.team_id = :team_id", nativeQuery = true)
        List<Schedule> findByTeamId(@Param("team_id") Long team_id);

        @Query(value = "SELECT * FROM Schedule s WHERE s.start_date = :start_date and s.end_date = :end_date", nativeQuery = true)
        List<Schedule> findByStartDateAndEndDate(@Param("start_date") LocalDate start_date,
                        @Param("end_date") LocalDate end_date);

        @Query(value = "SELECT * FROM Schedule s WHERE s.team_id = :team_id and s.start_date = :start_date", nativeQuery = true)
        Schedule findByTeamIdAndStartDate(@Param("team_id") Long team_id, @Param("start_date") LocalDate start_date);

        @Query(value = "SELECT * FROM Schedule s WHERE s.team_id = :team_id and (s.start_date between :start_date and :end_date or s.end_date between :start_date and :end_date)", nativeQuery = true)
        List<Schedule> findWfhConflicts(@Param("team_id") Long team_id, @Param("start_date") LocalDate start_date,
                        @Param("end_date") LocalDate end_date);

        @Query(value = "SELECT * FROM Schedule s WHERE (s.team_id = :team_id or s.mode = :mode) and (:start_date between s.start_date and s.end_date or :end_date between s.start_date and s.end_date)", nativeQuery = true)
        List<Schedule> findWfoConflicts(@Param("team_id") Long team_id, @Param("mode") int mode,
                        @Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date);
}
