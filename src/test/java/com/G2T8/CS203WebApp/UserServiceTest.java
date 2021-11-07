package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.service.UserService;
import com.G2T8.CS203WebApp.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.jpa.JpaSystemException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository users;
    @InjectMocks
    private UserService userService;

    @AfterEach
    void tearDown() {
        users.deleteAll();
    }

    @Test
    public void getUser_noIdFound_ThrowUserNotFoundException() {
        // arrange
        User mockUser = new User();
        mockUser.setID(Long.valueOf(1));
        mockUser.setRole("ROLE_ADMIN");
        mockUser.setName("Test Admin");
        mockUser.setEmail("test_admin@gmail.com");
        mockUser.setPassword("password");
        mockUser.setFirstLogin(false);

        final Long inputId = Long.valueOf(2);

        Optional<User> optEmpty = Optional.empty();

        when(users.findById(any(Long.class))).thenReturn(optEmpty);

        // act & assert
        Exception expected = assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(inputId);
        });

        assertEquals(expected.getMessage(), "Could not find user with id " + inputId);
        verify(users, never()).findById(mockUser.getID());
        verify(users).findById(inputId);
    }

    @Test
    public void updateUserVaccinationStatus_vaccinationStatusIsNot012_ThrowIllegalArgumentException() {
        // arrange
        User mockUser = new User();
        final Long userId = Long.valueOf(2);
        mockUser.setID(userId);
        mockUser.setRole("ROLE_ADMIN");
        mockUser.setName("Test Admin");
        mockUser.setEmail("test_admin@gmail.com");
        mockUser.setPassword("password");
        mockUser.setFirstLogin(false);

        Optional<User> opt = Optional.of(mockUser);

        when(users.findById(userId)).thenReturn(opt);

        final int vaccinationStatus = 3;

        // act & assert
        Exception expected = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserVaccinationStatus(userId, vaccinationStatus);
        });

        mockUser.setVaccinationStatus(vaccinationStatus);
        assertEquals(expected.getMessage(), "Vaccination Status must be 0, 1, or 2");
        verify(users, never()).save(mockUser);
    }

    @Test
    public void updateRole_InvalidRole_ThrowIllegalArgumentException() {
        // arrange
        User mockUser = new User();
        final Long userId = Long.valueOf(2);
        mockUser.setID(userId);
        mockUser.setRole("ROLE_ADMIN");
        mockUser.setName("Test Admin");
        mockUser.setEmail("test_admin@gmail.com");
        mockUser.setPassword("password");
        mockUser.setFirstLogin(false);

        Optional<User> opt = Optional.of(mockUser);

        when(users.findById(userId)).thenReturn(opt);

        final String invalidRole = "ROLE_DUMMY";
        // act & assert
        Exception expected = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateRole(userId, invalidRole);
        });

        mockUser.setRole(invalidRole);
        assertEquals(expected.getMessage(), "Role must be ROLE_BASIC or ROLE_ADMIN");
        verify(users, never()).save(mockUser);
    }

}
