package UnitTests;

import DataAccess.*;
import Model.*;
import Request.LoadRequest;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.PersonIDResponse;
import Result.PersonResponse;
import Result.Response;
import Result.UserResponse;
import Services.LoadService;
import Services.LoginService;
import Services.PersonService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

public class LoadServiceTest {
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
    @DisplayName("Data loaded successfully")
    void loadData_Success() {
        String georgeUUID = UUID.randomUUID().toString();
        User users[] = new User[1];
        users[0] = new User("george", "pass", "g@gmail.com",
                "George", "Castanza", "m", georgeUUID);
        Person persons[] = new Person[2];
        persons[0] = new Person(georgeUUID, "george", "George", "Castanza",
                "m", null ,null ,null);
        persons[1] = new Person(UUID.randomUUID().toString(), "george", "Elizabeth",
                "Frank", "f", null, null, null);
        Event events[] = new Event[1];
        events[0] = new Event(UUID.randomUUID().toString(), "george", georgeUUID, 500, 340, "France",
                "Paris", "Birth", 1950);
        LoadRequest request = new LoadRequest(users, persons, events);
        Response response = null;
        try {
            LoadService service = new LoadService(conn);
            response = service.loadData(request);
        }
        catch (Response r) {
            throw new AssertionError(r.message);
        }
        String expected = "Successfully added 2 persons and 1 events to the database.";
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.success);
        Assertions.assertEquals(expected, response.message);
    }
}
