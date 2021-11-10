package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import com.G2T8.CS203WebApp.exception.TemperatureInvalidException;
import com.G2T8.CS203WebApp.model.CustomUserDetails;
import com.G2T8.CS203WebApp.model.Temperature;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import com.G2T8.CS203WebApp.service.TemperatureService;
import com.G2T8.CS203WebApp.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TemperatureServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TemperatureRepository temperatureRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private TemperatureService temperatureService;

    private static User adminUser;
    private static User basicUser;

    private LocalDateTime defaultLocalDateTime = LocalDateTime.of(2021, 1, 1, 12, 0);

    @BeforeAll
    public static void setup() {
        adminUser = new User();
        adminUser.setID(Long.valueOf(1));
        adminUser.setRole("ROLE_ADMIN");
        adminUser.setName("Test Admin");
        adminUser.setEmail("test_admin@gmail.com");
        adminUser.setPassword("password");
        adminUser.setFirstLogin(false);

        basicUser = new User();
        basicUser.setID(Long.valueOf(2));
        basicUser.setRole("ROLE_BASIC");
        basicUser.setName("Test Basic");
        basicUser.setEmail("test_basic@gmail.com");
        basicUser.setPassword("password");
        basicUser.setFirstLogin(false);
    }

    /**
     * Clear database after every test
     */
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        temperatureRepository.deleteAll();
    }

    @Test
    public void getAllTemperatureByUserId_ReturnTempLog() {
        Temperature temperature1 = new Temperature();
        temperature1.setDate(LocalDateTime.now());
        temperature1.setUser(adminUser);
        temperature1.setTemperature(36);

        Temperature temperature2 = new Temperature();
        temperature1.setDate(LocalDateTime.now());
        temperature1.setUser(basicUser);
        temperature1.setTemperature(36);

        List<Temperature> basicUserListTemp = new ArrayList<Temperature>();

        basicUserListTemp.add(temperature1);
        basicUserListTemp.add(temperature2);

        Long basicUserID = basicUser.getID();

        when(temperatureRepository.findByUser(basicUser)).thenReturn(basicUserListTemp);
        when(userService.getUser(basicUserID)).thenReturn(basicUser);

        // act
        List<Temperature> getTemperaturebyUserIDResult = temperatureService.getAllTempbyUserID(basicUserID);

        // assert
        assertEquals(basicUserListTemp, getTemperaturebyUserIDResult);
        verify(temperatureRepository).findByUser(basicUser);
        verify(userService).getUser(basicUserID);
    }

    @Test
    public void addTemperature_ReturnTemperature() {
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);

            Temperature newTemperature = new Temperature();
            newTemperature.setDate(LocalDateTime.now());
            newTemperature.setTemperature(36.0);
            newTemperature.setUser(basicUser);

            when(userService.loadUserByUsername(any(String.class))).thenReturn(new CustomUserDetails(basicUser));
            when(temperatureRepository.save(any(Temperature.class))).thenReturn(newTemperature);

            Temperature temperature = temperatureService.addTemperature(basicUser.getEmail(), 36.0);

            assertNotNull(temperature);
            verify(temperatureRepository).save(newTemperature);
        }
    }

}
