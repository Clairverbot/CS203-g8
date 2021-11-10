package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import com.G2T8.CS203WebApp.model.CustomUserDetails;
import com.G2T8.CS203WebApp.model.CovidHistory;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.repository.CovidHistoryRepository;
import com.G2T8.CS203WebApp.service.CovidHistoryService;
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
public class CovidHistoryServiceTest {

    @Mock
    private CovidHistoryRepository covidHistoryRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CovidHistoryService covidHistoryService;

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
        covidHistoryRepository.deleteAll();
    }

    @Test
    public void getAllCovidHistories_ReturnAllCovidHistories(){
        //arrange

        LocalDateTime current = LocalDateTime.now();
        CovidHistory covidHistory1 = new CovidHistory(); 
        covidHistory1.setContractedDate(current);
        covidHistory1.setRecoverDate(current);
        covidHistory1.setUser(adminUser);

        CovidHistory covidHistory2 = new CovidHistory();
        covidHistory2.setContractedDate(current);
        covidHistory2.setRecoverDate(current);
        covidHistory2.setUser(basicUser);

        List <CovidHistory> listCovidHistory = new ArrayList<CovidHistory>();
        listCovidHistory.add(covidHistory1);
        listCovidHistory.add(covidHistory2);

        when(covidHistoryRepository.findAll()).thenReturn(listCovidHistory);

        // act
        List <CovidHistory> allCovidHistory = covidHistoryService.getAllCovidHistories();
        

        // assert
        assertNotNull(allCovidHistory);
        assertEquals(listCovidHistory,allCovidHistory);
        verify(covidHistoryRepository).findAll(); 


    }

    // @Test
    // public void addCovidHistory_NewCovidHistory_ReturnCovidHistory(){
    //     try(MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)){
    //         //arrange
    //         mocked.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);
    //         CovidHistory newcovidHistory1 = new CovidHistory();
    //         LocalDateTime current = LocalDateTime.now();
    //         newcovidHistory1.setContractedDate(current);
    //         newcovidHistory1.setRecoverDate(current);
    //         newcovidHistory1.setUser(adminUser);

    //         when(userService.loadUserByUsername(any(String.class))).thenReturn(new CustomUserDetails(adminUser));
    //         when(covidHistoryRepository.save(any(CovidHistory.class))).thenReturn(newcovidHistory1);

    //         // act
            
    //         try {
    //             CovidHistory covidHistoryresult = covidHistoryService.addCovidHistory(newcovidHistory1);
    //             // assert
    //             assertNotNull(covidHistoryresult);
    //             assertEquals(newcovidHistory1, covidHistoryresult);
    //             verify(userService).loadUserByUsername(adminUser.getEmail());
    //             verify(covidHistoryRepository).save(covidHistoryresult);
    //         } catch (MessagingException | IOException e) {
    //             // TODO Auto-generated catch block
    //             e.printStackTrace();
    //         }

            

    //     }
    // }

}
