package com.G2T8.CS203WebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import org.springframework.data.repository.query.Param;

import com.G2T8.CS203WebApp.model.*;  

@Repository
public interface UserRepository extends JpaRepository<User,Long>{


    @Query(value = "SELECT * FROM User u WHERE u.email = ?1", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);


    
}
