package com.G2T8.CS203WebApp.repository;

import com.G2T8.CS203WebApp.model.ARTTestResult;
import com.G2T8.CS203WebApp.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.time.LocalDate;

@Repository
public interface ARTTestResultRepository extends JpaRepository<ARTTestResult, Long>{

    List<ARTTestResult> findByUser(User user);

    List<ARTTestResult> findByUserAndWeeksMonday(User user, LocalDate weeksMonday);
}
