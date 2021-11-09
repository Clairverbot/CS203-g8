package com.G2T8.CS203WebApp.repository;

import com.G2T8.CS203WebApp.model.ARTTestResults;
import com.G2T8.CS203WebApp.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.time.LocalDate;

@Repository
public interface ARTTestResultRepository extends JpaRepository<ARTTestResults, Long>{

    List<ARTTestResults> findByUser(User user);

    List<ARTTestResults> findByUserAndWeeksMonday(User user, LocalDate weeksMonday);
}
