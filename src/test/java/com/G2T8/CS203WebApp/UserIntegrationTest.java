package com.G2T8.CS203WebApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import net.minidev.json.JSONObject;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.RestAssured.*;

import com.G2T8.CS203WebApp.repository.PasswordResetRepository;
import com.G2T8.CS203WebApp.repository.TeamRepository;
import com.G2T8.CS203WebApp.repository.UserRepository;
import com.G2T8.CS203WebApp.service.UserService;
import com.G2T8.CS203WebApp.configuration.JwtTokenUtil;
import com.G2T8.CS203WebApp.model.PasswordResetToken;
import com.G2T8.CS203WebApp.model.Team;
import com.G2T8.CS203WebApp.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";
    private final String baseEndpoint = "/api/v1/users";

    @Autowired
    private UserRepository users;

    @Autowired
    private TeamRepository teams;

    @Autowired
    private PasswordResetRepository passwordResetTokens;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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

    // --------------------------------------------------
    // Tests for retrieving user information
    // --------------------------------------------------

    @Test
    public void findUserByID_Success() throws Exception {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("test420@cs203g2t8.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        // Save dummy user into database
        final User user = users.save(testUser);

        UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        // Issue get request
        given().contentType("application/json").pathParam("id", user.getID())
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(userDetails))
                .get(baseUrl + port + baseEndpoint + "/{id}")
                // Expected response
                .then().statusCode(200);

        // Clean up dummy user from DB
        users.delete(user);
    }

    @Test
    public void getCurrentUser_success() {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("test420@cs203g2t8.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        // Save dummy user into database
        final User user = users.save(testUser);

        UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        // Issue get request
        Integer userId = given().contentType("application/json")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(userDetails))
                .get(baseUrl + port + baseEndpoint + "/current")
                // Expected response
                .then().statusCode(200).extract().path("id");

        assertEquals(user.getID(), Long.valueOf(userId));

        // Clean up dummy user from DB
        users.delete(user);
    }

    // --------------------------------------------------
    // Tests for updating user information
    // --------------------------------------------------
    @Test
    public void updateVaccinationStatus_success() {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("test420@cs203g2t8.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        // Save dummy user into database
        final User user = users.save(testUser);

        UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        final int newVaccinationStatus = 2;

        // Issue get request
        int vaccinationStatus = given().contentType("application/json")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(userDetails))
                .pathParam("id", user.getID()).body(newVaccinationStatus)
                .put(baseUrl + port + baseEndpoint + "/{id}/vaccination-status")
                // Expected response
                .then().statusCode(200).extract().path("vaccinationStatus");

        assertEquals(newVaccinationStatus, vaccinationStatus);

        // Clean up dummy user from DB
        users.delete(user);
    }

    @Test
    public void updateName_success() {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("test420@cs203g2t8.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        // Save dummy user into database
        final User user = users.save(testUser);

        UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        final String newName = "Test User";

        // Issue get request
        String name = given().contentType("application/json")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(userDetails))
                .pathParam("id", user.getID()).body(newName).put(baseUrl + port + baseEndpoint + "/{id}/name")
                // Expected response
                .then().statusCode(200).extract().path("name");

        assertEquals(newName, name);

        // Clean up dummy user from DB
        users.delete(user);
    }

    @Test
    public void updateRole_success() {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("test420@cs203g2t8.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        // Save dummy user into database
        final User user = users.save(testUser);

        UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        final String newRole = "ROLE_BASIC";

        // Issue get request
        String role = given().contentType("application/json")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(userDetails))
                .pathParam("id", user.getID()).body(newRole).put(baseUrl + port + baseEndpoint + "/{id}/role")
                // Expected response
                .then().statusCode(200).extract().path("role");

        assertEquals(newRole, role);

        // Clean up dummy user from DB
        users.delete(user);
    }

    @Test
    public void updateManager_success() {
        // Arrange
        // Create dummy user entity
        User testManager = new User();
        testManager.setEmail("test420@cs203g2t8.com");
        testManager.setName("Test Admin");
        testManager.setPassword(passwordEncoder.encode("password"));
        testManager.setRole("ROLE_ADMIN");
        testManager.setFirstLogin(true);

        User testEmployee = new User();
        testEmployee.setEmail("test690@cs203g2t8.com");
        testEmployee.setName("Test");
        testEmployee.setPassword(passwordEncoder.encode("password"));
        testEmployee.setRole("ROLE_BASIC");
        testEmployee.setFirstLogin(true);

        // Save dummy user into database
        User userManager = users.save(testManager);
        User userEmployee = users.save(testEmployee);

        UserDetails userDetails = userService.loadUserByUsername(testManager.getEmail());

        // Issue put request
        given().contentType("application/json")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(userDetails))
                .pathParam("id", userEmployee.getID()).body(userManager.getID())
                .put(baseUrl + port + baseEndpoint + "/{id}/manager")
                // Expected response
                .then().statusCode(200);

        userEmployee = userService.findByEmail("test690@cs203g2t8.com");
        assertEquals(userEmployee.getManagerUser().getID(), userManager.getID());

        // Clean up dummy user from DB
        users.delete(userEmployee);
        users.delete(userManager);
    }

    @Test
    public void updateTeam_success() {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("test420@cs203g2t8.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        Team testTeam = new Team();
        testTeam.setName("Test Team");

        // Save dummy user into database
        User user = users.save(testUser);
        final Team team = teams.save(testTeam);

        UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        // Issue put request
        given().contentType("application/json")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(userDetails))
                .pathParam("id", user.getID()).body(team.getTeamID()).put(baseUrl + port + baseEndpoint + "/{id}/team")
                // Expected response
                .then().statusCode(200);

        user = userService.findByEmail("test420@cs203g2t8.com");
        assertEquals(user.getTeam().getTeamID(), team.getTeamID());

        // Clean up dummy user & team
        users.delete(user);
        teams.delete(team);
    }

    // --------------------------------------------------
    // Tests for reset password
    // --------------------------------------------------
    @Test
    public void getResetPasswordToken_success() {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("test420@cs203g2t8.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        // Save dummy user into database
        User user = users.save(testUser);

        // Issue post request
        given().contentType("application/json").body(testUser.getEmail())
                .post(baseUrl + port + baseEndpoint + "/reset-password/token")
                // Expected response
                .then().statusCode(201);

        PasswordResetToken passResetToken = passwordResetTokens.findByUser(user);

        assertNotNull(passResetToken);

        // Clean up dummy user
        users.deleteById(user.getID());
    }

    @Test
    public void resetPassword_MalformedRequestBody_ReturnBadRequest() {
        // Arrange
        // Create dummy user entity
        User testUser = new User();
        testUser.setEmail("noreply.cs203g2t8.smu@gmail.com");
        testUser.setName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser.setFirstLogin(true);

        // Save dummy user into database
        User user = users.save(testUser);

        // Create password reset token
        String token = "testing-token";

        PasswordResetToken passResetToken = new PasswordResetToken(token, user);
        user.setPasswordResetToken(passResetToken);

        // Create request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("token", token);
        requestBody.put("password", "mynewpassword");

        // Issue put request
        given().contentType("application/json").body(requestBody).put(baseUrl + port + baseEndpoint + "/reset-password")
                // Expected response
                .then().statusCode(400);

        // Clean up dummy user
        users.deleteById(user.getID());
    }
}
