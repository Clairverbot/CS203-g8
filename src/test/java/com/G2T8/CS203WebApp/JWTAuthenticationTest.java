package com.G2T8.CS203WebApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.G2T8.CS203WebApp.configuration.*;
import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JWTAuthenticationTest {

    private static final String JWT_SECRET = "cs203g2t8";
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        this.jwtTokenUtil = new JwtTokenUtil(JWT_SECRET);
    }

    @Test
    public void generateToken_NormalConditions_TokenNotNull() {
        // Arrange
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails userDetails = new User("test@gmail.com", "password", authorities);

        // Act
        String token = jwtTokenUtil.generateToken(userDetails);

        // Assert
        assertThat(token).isNotEmpty();
    }

}
