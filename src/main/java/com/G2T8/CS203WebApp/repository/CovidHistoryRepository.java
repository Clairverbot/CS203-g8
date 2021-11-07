package com.G2T8.CS203WebApp.repository;
import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.LocalDateTime;
//working
@Repository
public interface CovidHistoryRepository extends JpaRepository<CovidHistory, Long>{

    @Query(value = "SELECT * FROM covid_history c WHERE c.userid = :userid", nativeQuery= true)
    List<CovidHistory> findByUserId(@Param("userid") Long userid);



}
    

