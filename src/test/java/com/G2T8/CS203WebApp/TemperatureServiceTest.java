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

import com.G2T8.CS203WebApp.model.CustomUserDetails;
import com.G2T8.CS203WebApp.model.Temperature;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.repository.TemperatureRepository;
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
    private TemperatureRepository temperatures;
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
        temperatures.deleteAll();
    }

    @Test
    public void getAllTemp_ReturnAllTemperatureLog() {
        // arrange
        Temperature temperature1 = new Temperature();
        temperature1.setTempId(Long.valueOf(1));
        temperature1.setDate(LocalDateTime.now());
        temperature1.setTemperature(36.0);
        temperature1.setUser(adminUser);

        Temperature temperature2 = new Temperature();
        temperature2.setTempId(Long.valueOf(2));
        temperature2.setDate(LocalDateTime.now());
        temperature2.setTemperature(36.0);
        temperature2.setUser(basicUser);

        List<Temperature> listTemp = new ArrayList<Temperature>();

        listTemp.add(temperature1);
        listTemp.add(temperature2);

        when(temperatures.findAll()).thenReturn(listTemp);

        // act
        List<Temperature> allTemps = temperatureService.getAllTemp();

        // assert
        assertNotNull(allTemps);
        assertEquals(listTemp, allTemps);
        verify(temperatures).findAll();
    }

    @Test
    public void addTemperature_NewTemperature_ReturnTempLog() {
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            // arrange
            mocked.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);

            Temperature newTemperature = new Temperature();
            newTemperature.setDate(LocalDateTime.now());
            newTemperature.setTemperature(36.0);
            newTemperature.setUser(adminUser);

            when(userService.loadUserByUsername(any(String.class))).thenReturn(new CustomUserDetails(adminUser));
            when(temperatures.save(any(Temperature.class))).thenReturn(newTemperature);

            // act
            Temperature temperature = temperatureService.addTemperature(adminUser.getEmail(), 36.0);

            // assert
            assertNotNull(temperature);
            verify(userService).loadUserByUsername(adminUser.getEmail());
            verify(temperatures).save(newTemperature);
        }
    }

    @Test
    public void getUserTempBetweenDateTime_lowerBoundAfterUpperBound_throwException() {
        // arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // act & assert
        Exception expected = assertThrows(IllegalArgumentException.class, () -> {
            temperatureService.getUserTempBetweenDateTime(adminUser,
                    LocalDateTime.parse("2021-01-01 19:00:00", formatter),
                    LocalDateTime.parse("2021-01-01 12:00:10", formatter));
        });

        assertEquals(expected.getMessage(), "Lower bound for date should be lower than upper bound");
        verify(temperatures, never()).findAllByUserAndDateBetween(adminUser,
                LocalDateTime.parse("2021-01-01 19:00:00", formatter),
                LocalDateTime.parse("2021-01-01 12:00:10", formatter));

    }

    // @Test
    // public void getUserTempBetweenDateTime_something_something() {
    // // arrange
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
    // HH:mm:ss");

    // Temperature temperature1 = new Temperature();
    // temperature1.setTempId(Long.valueOf(1));
    // temperature1.setDate(LocalDateTime.parse("2021-01-01 12:00:10", formatter));
    // temperature1.setTemperature(36.0);
    // temperature1.setUser(adminUser);

    // Temperature temperature2 = new Temperature();
    // temperature2.setTempId(Long.valueOf(2));
    // temperature2.setDate(LocalDateTime.parse("2021-01-01 19:00:00", formatter));
    // temperature2.setTemperature(36.0);
    // temperature2.setUser(adminUser);

    // List<Temperature> between = new ArrayList<>();
    // between.add(temperature1);
    // between.add(temperature2);

    // when(temperatures.findAllByUserAndDateBetween(adminUser,
    // any(LocalDateTime.class), any(LocalDateTime.class)))
    // .thenReturn(between);
    // }
}
