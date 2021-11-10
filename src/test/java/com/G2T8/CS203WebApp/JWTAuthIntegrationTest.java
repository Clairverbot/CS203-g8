// package com.G2T8.CS203WebApp;

// import static org.junit.jupiter.api.Assertions.*;

// import java.net.URI;
// import java.util.Optional;

// import org.hamcrest.core.Is;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.web.server.LocalServerPort;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import io.restassured.RestAssured;
// import io.restassured.config.JsonConfig;
// import io.restassured.path.json.config.JsonPathConfig;
// import io.restassured.response.Response;
// import net.minidev.json.JSONObject;
// import static io.restassured.config.RedirectConfig.redirectConfig;
// import static org.hamcrest.Matchers.equalTo;

// import static io.restassured.RestAssured.*;

// import com.G2T8.CS203WebApp.repository.UserRepository;
// import com.G2T8.CS203WebApp.model.User;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class JWTAuthIntegrationTest {

//     @LocalServerPort
//     private int port;

//     private final String baseUrl = "http://localhost:";
//     private final String baseEndpoint = "/api/v1/users";

//     @Autowired
//     private UserRepository users;

//     @Autowired
//     private BCryptPasswordEncoder passwordEncoder;

//     @BeforeAll
//     public static void initClass() {
//         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//         RestAssured.useRelaxedHTTPSValidation();
//         RestAssured.urlEncodingEnabled = false;
//         RestAssured.config = RestAssured.config()
//                 .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
//                 .redirect(redirectConfig().followRedirects(false));
//     }

//     /**
//      * Clear database after every test
//      */
//     @AfterEach
//     void tearDown() {
//         users.deleteAll();
//     }

//     // --------------------------------------------------
//     // Tests for saving user into database
//     // --------------------------------------------------

//     @Test
//     public void saveUser_Success() throws Exception {
//         URI uri = new URI(baseUrl + port + baseEndpoint + "/register");

//         // Create request body in JSON
//         JSONObject requestBody = new JSONObject();
//         requestBody.put("email", "test420@gmail.com");
//         requestBody.put("name", "Test");
//         requestBody.put("password", "whatpassword");
//         requestBody.put("role", "ROLE_EMPLOYEE");

//         // Issue post request
//         given().contentType("application/json").body(requestBody.toJSONString()).post(uri)
//                 // Expected response
//                 .then().statusCode(201);
//     }

//     @Test
//     public void saveUser_MissingParameter_ReturnBadRequest() throws Exception {
//         URI uri = new URI(baseUrl + port + baseEndpoint + "/register");

//         // Create request body in JSON -- missing name
//         JSONObject requestBody = new JSONObject();
//         requestBody.put("email", "test420@gmail.com");
//         requestBody.put("password", "whatpassword");
//         requestBody.put("role", "ROLE_EMPLOYEE");

//         // Issue post request
//         given().contentType("application/json").body(requestBody.toJSONString()).post(uri)
//                 // Expected response
//                 .then().statusCode(400);
//     }

//     @Test
//     public void saveUser_BlankParameter_ReturnBadRequest() throws Exception {
//         URI uri = new URI(baseUrl + port + baseEndpoint + "/register");

//         // Create request body in JSON -- blank email
//         JSONObject requestBody = new JSONObject();
//         requestBody.put("email", "");
//         requestBody.put("name", "Test");
//         requestBody.put("password", "whatpassword");
//         requestBody.put("role", "ROLE_EMPLOYEE");

//         // Issue post request
//         given().contentType("application/json").body(requestBody.toJSONString()).post(uri)
//                 // Expected response
//                 .then().statusCode(400);
//     }

//     @Test
//     public void saveUser_WhitespaceInput_ReturnBadRequest() throws Exception {
//         URI uri = new URI(baseUrl + port + baseEndpoint + "/register");

//         // Create request body in JSON -- password is whitespace
//         JSONObject requestBody = new JSONObject();
//         requestBody.put("email", "test420@gmail.com");
//         requestBody.put("name", "Test");
//         requestBody.put("password", " ");
//         requestBody.put("role", "ROLE_EMPLOYEE");

//         // Issue post request
//         given().contentType("application/json").body(requestBody.toJSONString()).post(uri)
//                 // Expected response
//                 .then().statusCode(400);
//     }

//     @Test
//     public void saveUser_EmailIsNotAnEmail_ReturnBadRequest() throws Exception {
//         URI uri = new URI(baseUrl + port + baseEndpoint + "/register");

//         // Create request body in JSON -- email does not follow email format
//         JSONObject requestBody = new JSONObject();
//         requestBody.put("email", "test");
//         requestBody.put("name", "Test");
//         requestBody.put("password", "password");
//         requestBody.put("role", "ROLE_EMPLOYEE");

//         // Issue post request
//         given().contentType("application/json").body(requestBody.toJSONString()).post(uri)
//                 // Expected response
//                 .then().statusCode(400);
//     }

//     // --------------------------------------------------
//     // Tests for logging in
//     // --------------------------------------------------

//     @Test
//     public void createAuthenticationToken_success() throws Exception {
//         // Arrange
//         URI uri = new URI(baseUrl + port + baseEndpoint + "/login");
//         // Create dummy user entity
//         User testUser = new User();
//         testUser.setID((long) 1);
//         testUser.setEmail("test420@gmail.com");
//         testUser.setName("Test");
//         testUser.setPassword(passwordEncoder.encode("password"));
//         testUser.setRole("ROLE_ADMIN");
//         // Save dummy user into database
//         users.save(testUser);

//         // Create request body in JSON
//         JSONObject requestBody = new JSONObject();
//         requestBody.put("email", "test420@gmail.com");
//         requestBody.put("password", "password");

//         // Issue post request
//         given().contentType("application/json").body(requestBody.toJSONString()).post(uri)
//                 // Expected response
//                 .then().statusCode(200);
//     }
// }
