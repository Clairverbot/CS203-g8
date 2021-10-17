package com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.LocalDateTime;

public interface OfficeRequestRepository extends JpaRepository<OfficeRequest, Long> {

    @Query(value = "SELECT o FROM officerequest o WHERE o.userid = user_id", nativeQuery = true)
    List<Optional<OfficeRequest>> findByUserId(@Param("user_id") Long user_id);

    @Query(value = "SELECT o FROM officerequest o WHERE o.userid = user_id and o.startDateTime = startDateTime", nativeQuery = true)
    Optional<OfficeRequest> findByUserIdAndStartDateTime(@Param("user_id") Long user_id,
            @Param("startDateTime") LocalDateTime startDateTimeOffice);
}
