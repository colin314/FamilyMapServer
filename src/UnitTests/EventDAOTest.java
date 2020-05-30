package UnitTests;

import DataAccess.*;
import Model.*;
import Result.FamilyMapException;
import Services.FillService;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class EventDAOTest {
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
    @DisplayName("Duplicate Event_ID Insert Fails")
    void insert_duplicateEventIDFails() {
        EventDAO eventDAO = new EventDAO(conn);
        Event newEvent1 = new Event("96319150-c039-48e0-8b3a-109f348fab85", "colin314", "personID", 15.0f, 15.0f,
                "USA", "Lodi", "Birth", 1990);
        Assertions.assertThrows(DataAccessException.class, () -> eventDAO.insert(newEvent1), "Failed to throw duplicate key exception");
    }

    @Test
    @DisplayName("Event successfully inserted")
    void insert_Success() {
        EventDAO eventDAO = new EventDAO(conn);
        String id = "NewID";
        Event newEvent = new Event(id, "colin314", "personID", 15.0f, 15.0f,
                "USA", "Lodi", "Birth", 1990);
        Assertions.assertDoesNotThrow(() -> eventDAO.insert(newEvent));
        Assertions.assertDoesNotThrow(() -> {
            ResultSet rs = conn.prepareStatement("SELECT * FROM Events WHERE Event_ID = 'NewID'")
                    .executeQuery();
            Assertions.assertTrue(rs.next(), "The event was not actually inserted into the database");
        });
    }

    @Test
    @DisplayName("Find Event returns correct event")
    void findByEventID_FindsEvent() {
        EventDAO eventDAO = new EventDAO(conn);
        Event expectedEvent = new Event("0e38556f-4a82-40ec-a2fd-1d48aaf04f48", "colin314",
                "f4378e53-24d4-42e4-97ab-91cbf8c74e24", -23.35f, 25.9167f,
                "Botswana", "Gaborone", "Death", 2016);
        Event actualEvent = null;
        try {
            actualEvent = eventDAO.find("0e38556f-4a82-40ec-a2fd-1d48aaf04f48");
        }
        catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
        Assertions.assertEquals(expectedEvent, actualEvent, "The event found is not the expected event.");
    }

    @Test
    @DisplayName("Find Event with invalid Event_ID returns null")
    void findByEventID_InvalidEventID_ReturnsNull() {
        EventDAO eventDAO = new EventDAO(conn);
        String uuid = UUID.randomUUID().toString();
        Event foundEvent = null;
        try {
            foundEvent = eventDAO.find(uuid);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            throw new AssertionError("EventDAO.find() threw an error.");
        }
        Assertions.assertNull(foundEvent, "EventDAO.find() returned a non-null object when Event_ID was invalid");
    }

    @Test
    @DisplayName("Find event by user returns expected list")
    void findByUser_ValidUser_Success() {
        EventDAO eventDAO = new EventDAO(conn);
        try {
            ArrayList<Event> expected = getExpectedEvents();
            ArrayList<Event> actual = eventDAO.findByUser("colin314");
            for (Event event:
                 actual) {
                Assertions.assertTrue(expected.contains(event), "Events associated with user are not equal to the expected events");
            }
        }
        catch (DataAccessException ex) {
            throw new AssertionError(ex.getMessage());
        }
        catch (FileNotFoundException ex) {
            throw new AssertionError(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Find event by user with invalid user returns empty list")
    void findByUser_InvalidUser_EmptyList() {
        EventDAO eventDAO = new EventDAO(conn);
        try {
            ArrayList<Event> actual = eventDAO.findByUser("badUser");
            Assertions.assertEquals(0, actual.size(), "An invalid username returned some records when none were expected.");
        }
        catch (DataAccessException ex) {
            throw new AssertionError(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Clear Events works")
    void EventClear_Works() {
        EventDAO eventDAO = new EventDAO(conn);
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events")){
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertTrue(rowCount > 3, "The initial state of the test DB is incorrect.");
            conn.rollback();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
        Assertions.assertDoesNotThrow(() -> eventDAO.clear(), "Clearing the events threw and exception");
    }

    @Test
    @DisplayName("Clear by user only clears users events")
    void clearByUser_Success() {
        EventDAO eventDAO = new EventDAO(conn);
        Assertions.assertDoesNotThrow(() -> eventDAO.clearByUser("colin314"), "Clearing the events threw and exception");
        ResultSet rs = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events")){
            rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertEquals(2, rowCount, "An incorrect number of records were removed");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
        finally {
            if (rs != null) {
                ResultSet finalRs = rs;
                Assertions.assertDoesNotThrow(() -> finalRs.close()); }
        }
    }

    @Test
    @DisplayName("Clear by user clears no records for bad username")
    void clearByUser_BadUser_NoRecordsCleared() {
        EventDAO eventDAO = new EventDAO(conn);
        Assertions.assertDoesNotThrow(() -> eventDAO.clearByUser("badUser"), "Clearing the events threw and exception");
        ResultSet rs = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events")){
            rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertEquals(93, rowCount, "Records were removed when none were supposed to be removed.");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
        finally {
            if (rs != null) {
                ResultSet finalRs = rs;
                Assertions.assertDoesNotThrow(() -> finalRs.close()); }
        }
    }

    private ArrayList<Event> getExpectedEvents() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader("Resources/familymapserver/json/eventsTest.json"));
        EventList events = (new Gson().fromJson(jsonReader, EventList.class));
        return events.data;
    }

    class EventList {
        ArrayList<Event> data;
    }
}
