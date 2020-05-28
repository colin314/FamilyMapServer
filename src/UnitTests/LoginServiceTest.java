package UnitTests;

import DataAccess.*;
import Model.*;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.PersonIDResponse;
import Result.PersonResponse;
import Result.Response;
import Result.UserResponse;
import Services.LoginService;
import Services.PersonService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

public class LoginServiceTest {
    private Connection conn = null;
    private static final String DICTIONARY = "dictionary.txt";
    private static final String SMALL_DICTIONARY = "small.txt";
    private static final String EMPTY_DICTIONARY = "empty.txt";
    private final String CONNECTION_URL = "jdbc:sqlserver://localhost\\DIPPR;" +
            "databaseName=FamilyMapDb_Tester;integratedSecurity=false;" +
            "encrypt=false;trustServerCertificate=true;" +
            "authentication=SqlPassword;user=sa;password=incorrect";

    @BeforeEach
    @DisplayName("Setting up test")
    void init() throws ExceptionInInitializerError {
        try {
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Cannot connect to testing db.");
        }
    }

    @AfterEach
    @DisplayName("Cleaning up after tests")
    void tearDown() throws SQLException {
        try {
            conn.rollback();
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Test
    @DisplayName("Login was successful")
    void loginAttempt_Success() {
        String userName = "colin314";
        String password = "password";
        LoginRequest request = new LoginRequest(userName, password);
        UserResponse response = null;
        try {
            LoginService service = new LoginService(conn);
            response = service.loginUser(request);
        }
        catch (Response r) {
            throw new AssertionError(r.message);
        }
        Assertions.assertNotNull(response);
        Assertions.assertEquals("62CC127E-477E-45D1-BF6A-B63FB89F075B", response.personID);
        Assertions.assertTrue(response.success);

        try {
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            String username = authTokenDAO.findUser(response.authToken);
            Assertions.assertEquals(userName, username);
        }
        catch (DataAccessException ex) {
            throw new AssertionError(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Bad login fails")
    void loginAttempt_Failure() {
        String userName = "colin314";
        String badPassword  = "Password";
        LoginRequest request = new LoginRequest(userName, badPassword);
        LoginService service = new LoginService(conn);
        Assertions.assertThrows(Response.class, () -> service.loginUser(request));

    }

}
