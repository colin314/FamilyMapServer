package UnitTests;

import DataAccess.*;
import Model.*;
import Request.RegisterRequest;
import Result.Response;
import Result.UserResponse;
import Services.ClearService;
import Services.RegisterService;
import Services.Service;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

public class ClearServiceTest {
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
    @DisplayName("Database successfully cleared.")
    void ClearSuccessful() {
        ClearService service = null;
        Response actual = null;

        try {
            service = new ClearService(conn);
            ClearService finalService = service;
            Assertions.assertDoesNotThrow(() -> finalService.clearDatabase());
        }
        catch (Response ex) {
            throw new AssertionError(ex.message);
        }
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users")){
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertEquals(0, rowCount, "Not all Users were cleared from the database");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events")){
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertEquals(0, rowCount, "Not all Events were cleared from the database");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Persons")){
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertEquals(0, rowCount, "Not all Persons were cleared from the database");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AuthTokens")){
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertEquals(0, rowCount, "Not all AuthTokens were cleared from the database");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
    }
}
