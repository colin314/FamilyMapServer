package UnitTests;

import DataAccess.*;
import Model.*;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.*;

public class PersonDAOTest {
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
    @DisplayName("Duplicate Person_ID Insert Fails")
    void testDuplicatePerson_ID() {
        Person newPerson1 = new Person("62CC127E-477E-45D1-BF6A-B63FB89F075B",
                "colin314", "Colin", "Anderson",
                "m", null, null, null);

        Assertions.assertThrows(DataAccessException.class, () -> personDAO.insert(newPerson1), "Failed to throw duplicate key exception");
        Person newPerson2 = new Person("21E6B772-74F1-43D8-B7D3-9306F08CC838",
                "colin314", "Colin", "Anderson",
                "m", null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> personDAO.insert(newPerson2), "Failed to throw duplicate key exception");
        Person newPerson3 = new Person("D8B340FC-F421-4472-8D08-FB28D3D5138F",
                "colin314", "Colin", "Anderson",
                "m", null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> personDAO.insert(newPerson3), "Failed to throw duplicate key exception");
    }

    @Test
    @DisplayName("Person successfully inserted")
    void testPersonInsert_Success() {
        String id = "ccb5c154-db20-4daa-a939-b2da51bbd343";
        Person newPerson = new Person(id,
                "colin314", "Ruth", "Anderson",
                "f", null, null, null);
        Assertions.assertDoesNotThrow(() -> personDAO.insert(newPerson));
        Assertions.assertDoesNotThrow(() -> {
            ResultSet rs = conn.prepareStatement("SELECT * FROM Persons WHERE Person_ID = 'ccb5c154-db20-4daa-a939-b2da51bbd343'")
                    .executeQuery();
            Assertions.assertDoesNotThrow(() -> rs.next(), "The person was not actually inserted into the database");
        });
    }

    @Test
    @DisplayName("Find Person returns correct person")
    void testPersonFind_FindsPerson() {
        Person expectedPerson = new Person("21E6B772-74F1-43D8-B7D3-9306F08CC838",
                "rDavis", "Rhen", "Davis", "m",
                null,null,null);
        Person actualPerson = null;
        try {
            actualPerson = personDAO.find("21E6B772-74F1-43D8-B7D3-9306F08CC838");
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            throw new AssertionError("PersonDAO.find() threw an error");
        }
        Assertions.assertEquals(expectedPerson, actualPerson, "The person returned by PersonDAO.find" +
                "did not have the requested Person_ID");
        Assertions.assertEquals(expectedPerson.firstName, actualPerson.firstName, "The first names did not match");
        Assertions.assertEquals(expectedPerson.lastName, actualPerson.lastName, "The last names did not match");
        Assertions.assertEquals(expectedPerson.associatedUsername, actualPerson.associatedUsername, "The associated usernames did not match");
        Assertions.assertEquals(expectedPerson.gender, actualPerson.gender, "The genders did not match");
        Assertions.assertEquals(expectedPerson.fatherID, actualPerson.fatherID, "The Father_IDs did not match");
        Assertions.assertEquals(expectedPerson.motherID, actualPerson.motherID, "The Mother_IDs did not match");
        Assertions.assertEquals(expectedPerson.spouseID, actualPerson.spouseID, "The Spouse_IDs did not match");
    }

    @Test
    @DisplayName("Find Person with invalid Person_ID returns null")
    void testPersonFind_InvalidPersonID_ReturnsNull() {
        String uuid = UUID.randomUUID().toString();
        Person foundPerson = null;
        try {
            foundPerson = personDAO.find(uuid);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            throw new AssertionError("PersonDAO.find() threw an error.");
        }
        Assertions.assertNull(foundPerson, "PersonDAO.find() returned a non-null object when Person_ID was invalid");

    }

    @Test
    @DisplayName("Clear Persons works")
    void PersonClear_Works() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Persons")){
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
        Assertions.assertDoesNotThrow(() -> personDAO.clear());
    }
}