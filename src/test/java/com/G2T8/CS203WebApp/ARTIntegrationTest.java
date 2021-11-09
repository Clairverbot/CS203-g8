package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.core.userdetails.UserDetails;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;

import com.G2T8.CS203WebApp.service.ARTTestResultService;
import com.G2T8.CS203WebApp.service.UserService;
import com.G2T8.CS203WebApp.repository.ARTTestResultRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import com.G2T8.CS203WebApp.model.ARTTestResults;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.configuration.JwtTokenUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ARTIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";
    private final String baseEndpoint = "/api/v1/art";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository users;

    @Autowired
    private UserService userService;

    @Autowired
    private ARTTestResultRepository artResults;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeAll
    public static void initClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
                .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
                .redirect(redirectConfig().followRedirects(false));
    }

    @Test
    public void findAllArt_Success() throws Exception {

        // Create dummy user
        User testUser = new User();
        testUser.setEmail("testUser@cs203g8.com");
        testUser.setName("Test User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        testUser = users.save(testUser);
        
        final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        // Create dummy art results
        LocalDateTime current = LocalDateTime.now();
        ARTTestResults artTestResult1 = new ARTTestResults();
        artTestResult1.setUser(testUser);
        artTestResult1.setArtResult(false);
        artTestResult1.setDate(current);
        artTestResult1.setWeeksMonday(current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        ARTTestResults artTestResult2 = new ARTTestResults();
        artTestResult2.setUser(testUser);
        artTestResult2.setArtResult(true);
        artTestResult2.setDate(current);
        artTestResult2.setWeeksMonday(current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        // Save dummy art results to artRepo
        artTestResult1 = artResults.save(artTestResult1);
        artTestResult2 = artResults.save(artTestResult2);

        // Get updated user from the db after saving new art test results
        testUser = users.getById(testUser.getID());

        URI uri = new URI(baseUrl + port + baseEndpoint + "/");

        // Issue get request
        given().contentType("application/json").header("Authorization", "Bearer " + token).get(uri)
                // Expected response
                .then().statusCode(200);

        users.delete(testUser);
    }

    @Test
    public void findAllArt_InvalidToken_Failure() throws Exception {

        String token = "fakeToken";

        URI uri = new URI(baseUrl + port + baseEndpoint + "/");

        // Issue get request
        given().contentType("application/json").header("Authorization", "Bearer " + token).get(uri)
                // Expected response
                .then().statusCode(401);
        
    }

    @Test
    public void findAllArt_NoAuthentication_Failure() throws Exception {

        URI uri = new URI(baseUrl + port + baseEndpoint + "/");

        // Issue get request
        given().contentType("application/json").get(uri)
                // Expected response
                .then().statusCode(401);
        
    }

    @Test
    public void findARTByUserId_Success() throws Exception {

        // Create dummy user
        User testUser = new User();
        testUser.setEmail("testUser@cs203g8.com");
        testUser.setName("Test User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);
        testUser = users.save(testUser);
        
        // Get user token
        final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Create dummy art results
        LocalDateTime current = LocalDateTime.now();
        ARTTestResults artTestResult1 = new ARTTestResults();
        artTestResult1.setUser(testUser);
        artTestResult1.setArtResult(false);
        artTestResult1.setDate(current);
        artTestResult1.setWeeksMonday(current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        ARTTestResults artTestResult2 = new ARTTestResults();
        artTestResult2.setUser(testUser);
        artTestResult2.setArtResult(true);
        artTestResult2.setDate(current);
        artTestResult2.setWeeksMonday(current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        // Save dummy art results to artRepo
        artResults.save(artTestResult1);
        artResults.save(artTestResult2);

        // Get updated user from the db after saving new art test results
        testUser = users.getById(testUser.getID());

        // Issue get request
        given().contentType("application/json").pathParam("userId", testUser.getID()).header("Authorization", "Bearer " + token).get(baseUrl + port + baseEndpoint + "/{userId}")
                // Expected response
                .then().statusCode(200);

        users.delete(testUser);
    }

    @Test
    public void findARTByUserId_InvalidUserId_Failure() throws Exception {

        // Create dummy user
        User testUser = new User();
        testUser.setEmail("testUser@cs203g8.com");
        testUser.setName("Test User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);
        users.save(testUser);
        
        // Get user token
        final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Create dummy art results
        LocalDateTime current = LocalDateTime.now();
        ARTTestResults artTestResult1 = new ARTTestResults();
        artTestResult1.setUser(testUser);
        artTestResult1.setArtResult(false);
        artTestResult1.setDate(current);
        artTestResult1.setWeeksMonday(current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        ARTTestResults artTestResult2 = new ARTTestResults();
        artTestResult2.setUser(testUser);
        artTestResult2.setArtResult(true);
        artTestResult2.setDate(current);
        artTestResult2.setWeeksMonday(current.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));

        // Save dummy art results to artResults
        artResults.save(artTestResult1);
        artResults.save(artTestResult2);

        // Get updated user from the db after saving new art test results
        testUser = users.getById(testUser.getID());

        // Issue get request
        given().contentType("application/json").pathParam("userId", 0L).header("Authorization", "Bearer " + token).get(baseUrl + port + baseEndpoint + "/{userId}")
                // Expected response
                .then().statusCode(404);

        users.delete(testUser);
    }

    @Test
    public void addART_Success() throws Exception {

        // Create dummy user
        User testUser = new User();
        testUser.setEmail("testUser@cs203g8.com");
        testUser.setName("Test User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);
        testUser = users.save(testUser);
        
        // Get user token
        final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Issue post request
        given().contentType("application/json").header("Authorization", "Bearer " + token).body(true).post(baseUrl + port + baseEndpoint + "/")
                // Expected response
                .then().statusCode(201);

        // Get updated user from the db after saving new art test result
        testUser = users.getById(testUser.getID());

        users.delete(testUser);
    }

    @Test
    public void addART_InvalidInput_Failure() throws Exception {

        // Create dummy user
        User testUser = new User();
        testUser.setEmail("testUser@cs203g8.com");
        testUser.setName("Test User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);
        testUser = users.save(testUser);
        
        // Get user token
        final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Issue post request
        given().contentType("application/json").header("Authorization", "Bearer " + token).body("not an ART test result").post(baseUrl + port + baseEndpoint + "/")
                // Expected response
                .then().statusCode(400);

        // Get updated user from the db after saving new art test result
        testUser = users.getById(testUser.getID());
        users.delete(testUser);

    }

    @Test
    public void addART_NotAuthenticated_Failure() throws Exception {

        // Issue post request
        given().contentType("application/json").body(true).post(baseUrl + port + baseEndpoint + "/")
                // Expected response
                .then().statusCode(401);

    }

    @Test
    public void getCountCurrentUserARTResultOnWeek_Success() throws Exception {

        // Create dummy user
        User testUser = new User();
        testUser.setEmail("testUser@cs203g8.com");
        testUser.setName("Test User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);
        testUser = users.save(testUser);
        
        // Get user token
        final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Issue get request
        given().contentType("application/json").queryParam("date", "2021-11-09").header("Authorization", "Bearer " + token).get(baseUrl + port + baseEndpoint + "/current/count-on-week")
                // Expected response
                .then().statusCode(200);

        // Get updated user from the db after saving new art test result
        testUser = users.getById(testUser.getID());
        users.delete(testUser);
    }

}
