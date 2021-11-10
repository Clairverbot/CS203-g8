package com.G2T8.CS203WebApp;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.restassured.path.json.config.JsonPathConfig;

import org.springframework.security.core.userdetails.UserDetails;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;

import static io.restassured.RestAssured.*;

import com.G2T8.CS203WebApp.service.UserService;
import com.G2T8.CS203WebApp.repository.TemperatureRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import com.G2T8.CS203WebApp.model.User;
import com.G2T8.CS203WebApp.configuration.JwtTokenUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TemperatureIntegrationTest {

  @LocalServerPort
  private int port;

  private final String baseUrl = "http://localhost:";
  private final String baseEndpoint = "/api/v1/temperature";

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

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
  public void getTemperatureByUserId_Success() throws Exception {
    // Create dummy user
    User testUser = new User();
    testUser.setEmail("testUser@cs203g8.com");
    testUser.setName("Test User");
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setRole("ROLE_ADMIN");
    testUser.setFirstLogin(true);
    testUser = userRepository.save(testUser);

    final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);

    URI uri = new URI(baseUrl + port + baseEndpoint + "/" + testUser.getID());
    given().contentType("application/json").header("Authorization", "Bearer " + token).get(uri).then().statusCode(200);

    testUser = userRepository.getById(testUser.getID());
    userRepository.delete(testUser);
  }

  @Test
  public void getTemperatureByUserId_UserNotFound() throws Exception {
    // Create dummy user
    User testUser = new User();
    testUser.setEmail("testUser@cs203g8.com");
    testUser.setName("Test User");
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setRole("ROLE_ADMIN");
    testUser.setFirstLogin(true);
    testUser = userRepository.save(testUser);

    final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);

    URI uri = new URI(baseUrl + port + baseEndpoint + "/-1");
    given().contentType("application/json").header("Authorization", "Bearer " + token).get(uri).then().statusCode(404);

    testUser = userRepository.getById(testUser.getID());
    userRepository.delete(testUser);
  }

  @Test
  public void getCurrentTemperatureCount_Success() throws Exception {
    // Create dummy user
    User testUser = new User();
    testUser.setEmail("testUser@cs203g8.com");
    testUser.setName("Test User");
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setRole("ROLE_ADMIN");
    testUser.setFirstLogin(true);
    testUser = userRepository.save(testUser);

    final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);

    URI uri = new URI(baseUrl + port + baseEndpoint + "/current/count?date=2021-11-11");
    given().contentType("application/json").header("Authorization", "Bearer " + token).get(uri).then().statusCode(200);

    testUser = userRepository.getById(testUser.getID());
    userRepository.delete(testUser);

  }

  @Test
  public void getCurrentTemperatureCount_InvalidDate() throws Exception {
    // Create dummy user
    User testUser = new User();
    testUser.setEmail("testUser@cs203g8.com");
    testUser.setName("Test User");
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setRole("ROLE_ADMIN");
    testUser.setFirstLogin(true);
    testUser = userRepository.save(testUser);

    final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);

    URI uri = new URI(baseUrl + port + baseEndpoint + "/current/count?date=2021-11-111");
    given().contentType("application/json").header("Authorization", "Bearer " + token).get(uri).then().statusCode(400);

    testUser = userRepository.getById(testUser.getID());
    userRepository.delete(testUser);

  }

  @Test
  public void addTemperature_Success() throws Exception {
    // Create dummy user
    User testUser = new User();
    testUser.setEmail("testUser@cs203g8.com");
    testUser.setName("Test User");
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setRole("ROLE_ADMIN");
    testUser.setFirstLogin(true);
    testUser = userRepository.save(testUser);

    final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);

    URI uri = new URI(baseUrl + port + baseEndpoint + "/");
    double temperature = 36;
    // Issue get request
    given().contentType("application/json").header("Authorization", "Bearer " + token).body(temperature).post(uri)
        .then().statusCode(201);

    testUser = userRepository.getById(testUser.getID());
    userRepository.delete(testUser);

  }

  @Test
  public void addTemperature_TemperatureLessThan33_BadRequest() throws Exception {
    // Create dummy user
    User testUser = new User();
    testUser.setEmail("testUser@cs203g8.com");
    testUser.setName("Test User");
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setRole("ROLE_ADMIN");
    testUser.setFirstLogin(true);
    testUser = userRepository.save(testUser);

    final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);

    URI uri = new URI(baseUrl + port + baseEndpoint + "/");
    double temperature = 32;
    given().contentType("application/json").header("Authorization", "Bearer " + token).body(temperature).post(uri)
        .then().statusCode(400);

    testUser = userRepository.getById(testUser.getID());
    userRepository.delete(testUser);

  }

  @Test
  public void addTemperature_TemperatureGreaterThan43_BadRequest() throws Exception {
    // Create dummy user
    User testUser = new User();
    testUser.setEmail("testUser@cs203g8.com");
    testUser.setName("Test User");
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setRole("ROLE_ADMIN");
    testUser.setFirstLogin(true);
    testUser = userRepository.save(testUser);

    final UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);

    URI uri = new URI(baseUrl + port + baseEndpoint + "/");
    double temperature = 44;
    given().contentType("application/json").header("Authorization", "Bearer " + token).body(temperature).post(uri)
        .then().statusCode(400);

    testUser = userRepository.getById(testUser.getID());
    userRepository.delete(testUser);
  }

}
