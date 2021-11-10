package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.G2T8.CS203WebApp.exception.UserNotFoundException;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.service.UserService;
import com.G2T8.CS203WebApp.repository.PasswordResetRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordResetRepository passwordResetRepository;
    @InjectMocks
    private UserService userService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void getUser_CorrectId_Succeed() {
        // arrange
        User mockUser = new User();
        final Long inputId = Long.valueOf(1);
        mockUser.setID(inputId);
        mockUser.setRole("ROLE_ADMIN");
        mockUser.setName("Test Admin");
        mockUser.setEmail("test_admin@gmail.com");
        mockUser.setPassword("password");
        mockUser.setFirstLogin(false);

        Optional<User> opt = Optional.of(mockUser);

        when(userRepository.findById(inputId)).thenReturn(opt);

        // act
        User user = userService.getUser(inputId);

        // assert
        assertEquals(mockUser, user);
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

        when(userRepository.findById(any(Long.class))).thenReturn(optEmpty);

        // act & assert
        Exception expected = assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(inputId);
        });

        assertEquals(expected.getMessage(), "Could not find user with id " + inputId);
        verify(userRepository, never()).findById(mockUser.getID());
        verify(userRepository).findById(inputId);
    }

    @Test
    public void updateUserVaccinationStatus_validVaccinationStatus_Success() {
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

        final int vaccinationStatus = 1;

        User mockUserUpdated = new User();
        mockUserUpdated.setID(userId);
        mockUserUpdated.setRole("ROLE_ADMIN");
        mockUserUpdated.setName("Test Admin");
        mockUserUpdated.setEmail("test_admin@gmail.com");
        mockUserUpdated.setPassword("password");
        mockUserUpdated.setFirstLogin(false);
        mockUserUpdated.setVaccinationStatus(vaccinationStatus);

        when(userRepository.findById(userId)).thenReturn(opt);
        when(userRepository.save(any(User.class))).thenReturn(mockUserUpdated);

        // act
        User user = userService.updateUserVaccinationStatus(userId, vaccinationStatus);

        // assert
        assertEquals(mockUserUpdated, user);
        verify(userRepository).save(user);
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

        when(userRepository.findById(userId)).thenReturn(opt);

        final int vaccinationStatus = 3;

        // act & assert
        Exception expected = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserVaccinationStatus(userId, vaccinationStatus);
        });

        mockUser.setVaccinationStatus(vaccinationStatus);
        assertEquals(expected.getMessage(), "Vaccination Status must be 0, 1, or 2");
        verify(userRepository, never()).save(mockUser);
    }

    @Test
    public void updateRole_ValidRole_ThrowIllegalArgumentException() {
        // arrange
        final String updatedRole = "ROLE_ADMIN";

        User mockUser = new User();
        final Long userId = Long.valueOf(2);
        mockUser.setID(userId);
        mockUser.setRole("ROLE_BASIC");
        mockUser.setName("Test");
        mockUser.setEmail("test_basic@gmail.com");
        mockUser.setPassword("password");
        mockUser.setFirstLogin(false);

        User mockUserUpdated = new User();
        mockUserUpdated.setID(userId);
        mockUserUpdated.setRole(updatedRole);
        mockUserUpdated.setName("Test");
        mockUserUpdated.setEmail("test_basic@gmail.com");
        mockUserUpdated.setPassword("password");
        mockUserUpdated.setFirstLogin(false);

        Optional<User> opt = Optional.of(mockUser);

        when(userRepository.findById(userId)).thenReturn(opt);
        when(userRepository.save(any(User.class))).thenReturn(mockUserUpdated);

        // act
        User user = userService.updateRole(userId, updatedRole);

        // assert
        assertEquals(mockUserUpdated, user);
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);

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

        when(userRepository.findById(userId)).thenReturn(opt);

        final String invalidRole = "ROLE_DUMMY";
        // act & assert
        Exception expected = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateRole(userId, invalidRole);
        });

        mockUser.setRole(invalidRole);
        assertEquals(expected.getMessage(), "Role must be ROLE_BASIC or ROLE_ADMIN");
        verify(userRepository, never()).save(mockUser);
    }

    @Test
    public void updateManagerId_ManageThemself_ThrowIllegalArgumentException() {
        // arrange
        User mockUser = new User();
        final Long userId = Long.valueOf(1);
        mockUser.setID(userId);
        mockUser.setRole("ROLE_ADMIN");
        mockUser.setName("Test Admin");
        mockUser.setEmail("test_admin@gmail.com");
        mockUser.setPassword("password");
        mockUser.setFirstLogin(false);

        // act
        Exception expected = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateManagerId(userId, userId);
        });

        assertEquals(expected.getMessage(), "Manager cannot have the same ID as user");
        verify(userRepository, never()).save(mockUser);
    }

    @Test
    public void updateManagerId_SetNonManagerAsManager_ThrowUserNotFoundException() {
        // arrange
        User mockUser1 = new User();
        final Long userId1 = Long.valueOf(1);
        mockUser1.setID(userId1);
        mockUser1.setRole("ROLE_BASIC");
        mockUser1.setName("Test User");
        mockUser1.setEmail("test_user@gmail.com");
        mockUser1.setPassword("password");
        mockUser1.setFirstLogin(false);

        User mockUser2 = new User();
        final Long userId2 = Long.valueOf(2);
        mockUser2.setID(userId2);
        mockUser2.setRole("ROLE_BASIC");
        mockUser2.setName("Test User 2");
        mockUser2.setEmail("test_user2@gmail.com");
        mockUser2.setPassword("password");
        mockUser2.setFirstLogin(false);

        Optional<User> opt1 = Optional.of(mockUser1);
        Optional<User> opt2 = Optional.of(mockUser2);

        when(userRepository.findById(userId1)).thenReturn(opt1);
        when(userRepository.findById(userId2)).thenReturn(opt2);

        // act & assert
        Exception expected = assertThrows(UserNotFoundException.class, () -> {
            userService.updateManagerId(userId1, userId2);
        });
        mockUser1.setManagerUser(mockUser2);
        assertEquals(expected.getMessage(), "Could not find user with id " + userId2);
        verify(userRepository, never()).save(mockUser1);
    }
}
