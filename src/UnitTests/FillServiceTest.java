package UnitTests;

import DataAccess.*;
import Result.FamilyMapException;
import Services.FillService;
import org.junit.jupiter.api.*;
import java.sql.*;

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
            var response = service.fillDatabase("colin314", 3);
            String expected = "Successfully added 15 persons and 45 events to the database.";
            Assertions.assertNotNull(response);
            Assertions.assertEquals(expected, response.message);
        }
        catch (FamilyMapException r) {
            throw new AssertionError(r.getMessage());
        }
    }

    @Test
    @DisplayName("Fill fails for bad user")
    void fill_BadUser() {
        FillService service = new FillService(conn);
        Assertions.assertThrows(FamilyMapException.class, () -> service.fillDatabase("badUser", 4));
        try {
            service.fillDatabase("badUser", 3);
        }
        catch (FamilyMapException ex) {
            Assertions.assertEquals("Error executing fill, the given username does not correspond to a known username. Given username: badUser",
                    ex.getMessage());
        }
    }

    @Test
    @DisplayName("Fill runs successfully with default value")
    void fillDefault_Success() {
        try {
            FillService service = new FillService(conn);
            var response = service.fillDatabase("colin314");
            String expected = "Successfully added 31 persons and 93 events to the database.";
            Assertions.assertNotNull(response);
            Assertions.assertEquals(expected, response.message);
        }
        catch (FamilyMapException r) {
            throw new AssertionError(r.getMessage());
        }
    }

    @Test
    @DisplayName("Fill fails for bad user and default value")
    void fillDefault_BadUser() {
        FillService service = new FillService(conn);
        Assertions.assertThrows(FamilyMapException.class, () -> service.fillDatabase("badUser"));
        try {
            service.fillDatabase("badUser");
        }
        catch (FamilyMapException ex) {
            Assertions.assertEquals("Error executing fill, the given username does not correspond to a known username. Given username: badUser",
                    ex.getMessage());
        }
    }
}
