package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import com.G2T8.CS203WebApp.model.ARTTestResults;
import com.G2T8.CS203WebApp.model.CustomUserDetails;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import com.G2T8.CS203WebApp.service.ARTTestResultService;
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
public class ARTTestResultServiceTest {
    @Mock
    private ARTTestResultRepository artResults;
    @Mock
    private UserService userService;

    @InjectMocks
    private ARTTestResultService artTestResultService;

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
        artResults.deleteAll();
    }

    @Test
    public void addART_NewArtResult_ReturnArtResult() {
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            // arrange
            mocked.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);

            ARTTestResults newARTResult = new ARTTestResults();
            LocalDateTime current = LocalDateTime.now();
            newARTResult.setUser(adminUser);
            newARTResult.setArtResult(false);
            newARTResult.setDate(current);
            newARTResult.setWeeksMonday(current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

            when(userService.loadUserByUsername(any(String.class))).thenReturn(new CustomUserDetails(adminUser));
            when(artResults.save(any(ARTTestResults.class))).thenReturn(newARTResult);

            // act
            ARTTestResults artTestResult = artTestResultService.addART(adminUser.getEmail(), false);

            // assert
            assertNotNull(artTestResult);
            verify(userService).loadUserByUsername(adminUser.getEmail());
            verify(artResults).save(newARTResult);

        }
    }
}
