package UnitTests;

import DataAccess.*;
import Model.*;
import Result.FamilyMapException;
import Result.PersonIDResponse;
import Result.PersonResponse;
import Services.EventService;
import Services.PersonService;
import Services.UnauthorizedException;
import org.junit.jupiter.api.*;
import java.sql.*;

public class PersonServiceTest {
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
    @DisplayName("Person found with valid ID and AuthToken")
    void findPerson_PersonFound() {
        String personID = "819c8edd-3a32-47b2-b404-fb456111ce8c";
        String authToken = "c0eb66bb-8ff9-4131-bef9-6090100dc1c2";
        PersonIDResponse expected = new PersonIDResponse(new Person(personID, "colin314",
                "Denita", "Pfeffer", "m", "5eb14bcb-938f-4862-b103-3e3d7a4e9eba",
                "45990374-43b6-4606-b8ed-aa786fb02cc2", "4048a7d8-93f1-457d-9500-5bc590e93861"));
        PersonIDResponse response;
        try {
            PersonService service = new PersonService(conn);
            response = service.getPersonByID(personID, authToken);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
        catch (UnauthorizedException ex) {
            throw new AssertionError(ex.getMessage());
        }
        Assertions.assertTrue(response.personID.equalsIgnoreCase(personID));
    }
    @Test
    @DisplayName("Person, user mismatch, returns bad response")
    void findEvent_BadUser() {
        String eventID = "21E6B772-74F1-43D8-B7D3-9306F08CC838";
        String authToken = "c0eb66bb-8ff9-4131-bef9-6090100dc1c2";
        try {
            PersonService service = new PersonService(conn);
            Assertions.assertThrows(FamilyMapException.class, () -> service.getPersonByID(eventID, authToken));
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
    }
    @Test
    @DisplayName("All Persons found for user")
    void findPersonByUser_AllPersonsFound() {
        String authToken = "C0EB66BB-8FF9-4131-BEF9-6090100DC1C2";
        PersonResponse response;
        try {
            PersonService service = new PersonService(conn);
            response = service.getPersonByUsername(authToken);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
        catch (UnauthorizedException ex) {
            throw new AssertionError(ex.getMessage());
        }
        Assertions.assertEquals(31, response.data.size());
    }
    @Test
    @DisplayName("Find all persons, invalid authToken fails")
    void findPersonByUser_InvalidAuthToken() {
        String authToken = "bad token";
        PersonResponse response;
        try {
            PersonService service = new PersonService(conn);
            Assertions.assertThrows(UnauthorizedException.class, () -> service.getPersonByUsername(authToken));
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
    }
}
