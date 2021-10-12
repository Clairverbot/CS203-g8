package com.G2T8.CS203WebApp.repository;

import com.G2T8.CS203WebApp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findFirstByToken(String token);

    User findByUser(User user);
}
