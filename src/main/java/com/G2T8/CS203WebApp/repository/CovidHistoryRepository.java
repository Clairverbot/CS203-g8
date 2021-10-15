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

    @Query(value = "SELECT * FROM covid_history c WHERE c.userid = userid", nativeQuery= true)
    List<Optional<CovidHistory>> findByUserId(@Param("userid") Long userid);

    @Query(value = "SELECT * FROM covid_history c WHERE c.userid = userid and c.contractedDate = contractedDate", nativeQuery = true)
    Optional<CovidHistory> findByUserIdAndContractedDate(@Param("userid") Long userid, 
            @Param("contractedDate") LocalDateTime contractedDate);

    //CovidHistory findByuseridAndcontractedDate(Long userid, Long contractedDate);


}
    

