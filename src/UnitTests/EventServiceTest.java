package UnitTests;

import DataAccess.*;
import Model.*;
import Result.EventIDResponse;
import Result.EventResponse;
import Result.FamilyMapException;
import Services.EventService;
import Services.UnauthorizedException;
import org.junit.jupiter.api.*;
import java.sql.*;

public class EventServiceTest {
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
    @DisplayName("Event found with valid ID and AuthToken")
    void findEvent_EventFound() {
        String eventID = "44B4C8E6-8261-42C0-8779-C646433B281A";
        String authToken = "C0EB66BB-8FF9-4131-BEF9-6090100DC1C2";
        EventIDResponse expected = new EventIDResponse(new Event(eventID, "colin314",
                "27E87F19-380A-4213-A38F-13E2529FF114", 150.5f, 160.7f, "United States",
                "Lodi", "Birth", 1962));
        EventIDResponse response;
        try {
            EventService service = new EventService(conn);
            response = service.getEventByID(eventID, authToken);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
        catch (UnauthorizedException ex) {
            throw new AssertionError(ex.getMessage());
        }
        Assertions.assertTrue(response.equals(expected));
    }
    @Test
    @DisplayName("All Events found for user")
    void findEventByUser_AllEventsFound() {
        String authToken = "C0EB66BB-8FF9-4131-BEF9-6090100DC1C2";
        EventResponse response;
        try {
            EventService service = new EventService(conn);
            response = service.getEventByUser(authToken);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
        catch (UnauthorizedException ex) {
            throw new AssertionError(ex.getMessage());
        }
        Assertions.assertEquals(2, response.data.size());
    }
}
