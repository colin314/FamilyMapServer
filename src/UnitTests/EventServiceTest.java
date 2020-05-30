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
        String eventID = "a8dede37-af8d-4683-be52-7fbea77079a2";
        String authToken = "c0eb66bb-8ff9-4131-bef9-6090100dc1c2";
        EventIDResponse expected = new EventIDResponse(new Event(eventID, "colin314",
                "d02f8ee2-fdf5-40f5-a531-5b55a1e6b8cb", 22.6333f, -120.2667f, "Taiwan",
                "Gaoxiong", "Birth", 1814));
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
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.equals(expected));
    }
    @Test
    @DisplayName("Event and user mismatch, throws exception")
    void findEvent_BadUser() {
        String eventID = "634d4e4d-96da-4506-9b8a-08794b39ba6d";
        String authToken = "c0eb66bb-8ff9-4131-bef9-6090100dc1c2";
        try {
            EventService service = new EventService(conn);
            Assertions.assertThrows(FamilyMapException.class, ()-> service.getEventByID(eventID, authToken));
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
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
        Assertions.assertEquals(91, response.data.size());
    }
    @Test
    @DisplayName("Find All Events with bad AuthToken throws exception")
    void findEventByUser_BadAuthToken() {
        String authToken = "bad token";
        try {
            EventService service = new EventService(conn);
            Assertions.assertThrows(UnauthorizedException.class, () -> service.getEventByUser(authToken));
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
    }
}
