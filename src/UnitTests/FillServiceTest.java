package UnitTests;

import DataAccess.*;
import Model.*;
import Request.RegisterRequest;
import Result.Response;
import Result.UserResponse;
import Services.FillService;
import Services.RegisterService;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

public class FillServiceTest {
    private Connection conn = null;
    private PersonDAO personDAO;
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
            personDAO = new PersonDAO(conn);
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
    @DisplayName("Fill runs successfully")
    void fill_Success() {
        try {
            FillService service = new FillService(conn);
            Response response = service.fillDatabase("colin314", 2);
            String expected = "Successfully added 5 persons and 15 events to the database.";
            Assertions.assertNotNull(response);
            Assertions.assertTrue(response.success);
            Assertions.assertEquals(expected, response.message);
        }
        catch (Response r) {
            throw new AssertionError(r.message);
        }
    }
}