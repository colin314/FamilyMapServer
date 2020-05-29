package UnitTests;

import DataAccess.*;
import Model.*;
import Result.FamilyMapException;
import Result.PersonIDResponse;
import Result.PersonResponse;
import Services.PersonService;
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
        String personID = "27E87F19-380A-4213-A38F-13E2529FF114";
        String authToken = "C0EB66BB-8FF9-4131-BEF9-6090100DC1C2";
        PersonIDResponse expected = new PersonIDResponse(new Person(personID, "colin314",
                "Neil", "Anderson", "m", null, null, null));
        PersonIDResponse response;
        try {
            PersonService service = new PersonService(conn);
            response = service.getPersonByID(personID, authToken);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.message);
        }
        Assertions.assertTrue(response.personID.equalsIgnoreCase(personID));
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
            throw new AssertionError(ex.message);
        }
        Assertions.assertEquals(3, response.data.size());
    }
}
