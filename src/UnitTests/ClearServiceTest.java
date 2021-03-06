package UnitTests;

import DataAccess.*;
import Result.FamilyMapException;
import Result.Response;
import Services.ClearService;
import org.junit.jupiter.api.*;
import java.sql.*;

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
        try {
            ClearService service = new ClearService(conn);
            Response actual = null;
            actual = service.clearDatabase();
            Assertions.assertNotNull(actual);
            Assertions.assertTrue(actual.success);
            Assertions.assertEquals("Clear succeeded.", actual.message);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
        //Verify that stuff was actually deleted
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

    @Test
    @DisplayName("Clear on a closed connection fails")
    void clear_ClosedConnection() {
        try {
            ClearService clearService = new ClearService(conn);
            conn.close();
            Assertions.assertThrows(FamilyMapException.class, () -> clearService.clearDatabase());
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
        catch (SQLException ex) {
            throw new AssertionError(ex.getMessage());
        }

    }
}
