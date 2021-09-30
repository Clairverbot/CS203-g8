package com.G2T8.CS203WebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.G2T8.CS203WebApp.model.*;  

@Repository
public interface UserRepository extends JpaRepository<User,Long>{


    
}
