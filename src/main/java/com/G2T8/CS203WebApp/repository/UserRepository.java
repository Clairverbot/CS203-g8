package com.G2T8.CS203WebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.G2T8.CS203WebApp.model.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * used to find a user using their email
     * 
     * @param email is the email of the user
     */
    public User findByEmail(String email);

}
