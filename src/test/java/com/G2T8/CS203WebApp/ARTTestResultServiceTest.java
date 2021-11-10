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

import com.G2T8.CS203WebApp.model.ARTTestResult;
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
    private ARTTestResultRepository artTestResultRepository;

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
        artTestResultRepository.deleteAll();
    }

    @Test
    public void getAllResult_ReturnAllARTResults() {
        // arrange
        LocalDateTime current = LocalDateTime.now();
        ARTTestResult artTestResult1 = new ARTTestResult(adminUser, false, current, current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));
        ARTTestResult artTestResult2 = new ARTTestResult(basicUser, false, current, current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        List<ARTTestResult> listART = new ArrayList<ARTTestResult>();

        listART.add(artTestResult1);
        listART.add(artTestResult2);

        when(artTestResultRepository.findAll()).thenReturn(listART);

        // act
        List<ARTTestResult> allART = artTestResultService.getAllResult();

        // assert
        assertEquals(listART, allART);
        verify(artTestResultRepository).findAll();

    }

    @Test
    public void addART_NewArtResult_ReturnArtResult() {
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            // arrange
            mocked.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);

            LocalDateTime current = LocalDateTime.now();
            ARTTestResult newARTResult = new ARTTestResult(adminUser, false, current, current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

            when(userService.loadUserByUsername(any(String.class))).thenReturn(new CustomUserDetails(adminUser));
            when(artTestResultRepository.save(any(ARTTestResult.class))).thenReturn(newARTResult);

            // act
            ARTTestResult artTestResult = artTestResultService.addART(adminUser.getEmail(), false);

            // assert
            assertNotNull(artTestResult);
            verify(userService).loadUserByUsername(adminUser.getEmail());
            verify(artTestResultRepository).save(newARTResult);

        }
    }

    @Test
    public void getARTbyUserID_ReturnAllUserResults() {
        /// arrange
        LocalDateTime current = LocalDateTime.now();
        ARTTestResult artTestResult1 = new ARTTestResult(adminUser, false, current, current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));
        ARTTestResult artTestResult2 = new ARTTestResult(basicUser, false, current, current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        List<ARTTestResult> basicUserListART = new ArrayList<ARTTestResult>();

        basicUserListART.add(artTestResult1);
        basicUserListART.add(artTestResult2);

        Long basicUserID = basicUser.getID();

        when(artTestResultRepository.findByUser(basicUser)).thenReturn(basicUserListART);
        when(userService.getUser(basicUserID)).thenReturn(basicUser);

        // act
        List<ARTTestResult> getARTbyUserIDResult = artTestResultService.getARTbyUserID(basicUserID);

        // assert
        assertEquals(basicUserListART, getARTbyUserIDResult);
        verify(artTestResultRepository).findByUser(basicUser);
        verify(userService).getUser(basicUserID);
    }
}
