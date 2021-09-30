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

    
}
