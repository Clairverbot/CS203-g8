package com.G2T8.CS203WebApp.repository;

import com.G2T8.CS203WebApp.model.Temperature;
import com.G2T8.CS203WebApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.LocalDateTime;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
    List<Temperature> findByUser(User user);

    List<Temperature> findAllByUserAndDateBetween(User user, LocalDateTime lowerBound, LocalDateTime upperBound);
}
