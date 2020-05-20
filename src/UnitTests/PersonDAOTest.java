package UnitTests;

import DataAccess.*;
import Model.*;
import org.junit.jupiter.api.*;
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
        String CONNECTION_URL = "jdbc:sqlserver://localhost\\DIPPR;" +
                "databaseName=FamilyMapDb_Tester;integratedSecurity=false;" +
                "encrypt=false;trustServerCertificate=true;" +
                "authentication=SqlPassword;user=sa;password=incorrect";
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
        Person newPerson1 = new Person("DF51D8E8-0FAE-4CEF-BFE3-52C318CD71DB",
                "colin314", "Colin", "Anderson",
                "m", null, null, null);

        Assertions.assertThrows(DataAccessException.class, () -> personDAO.insert(newPerson1), "Failed to throw duplicate key exception");
        Person newPerson2 = new Person("3E875825-EE87-4FC9-B993-06B4E10F4E07",
                "colin314", "Colin", "Anderson",
                "m", null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> personDAO.insert(newPerson2), "Failed to throw duplicate key exception");
        Person newPerson3 = new Person("45BF351E-5A61-4F94-856F-D69D0A09ACC4",
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
    @DisplayName("Person insertion fails when Username is not found in Users table")
    void testPersonInsert_UsernameFKFail() {
        Person newPerson = new Person(UUID.randomUUID().toString(), "invalidUser",
                "First", "Last", "m", null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> personDAO.insert(newPerson));
    }

    @Test
    @DisplayName("Find Person returns correct person")
    void testPersonFind_FindsPerson() {
        Person expectedPerson = new Person("3E875825-EE87-4FC9-B993-06B4E10F4E07",
                "rDavis", "Rhen", "Davis", "m",
                null,null,null);
        Person actualPerson = null;
        try {
            actualPerson = personDAO.find("3E875825-EE87-4FC9-B993-06B4E10F4E07");
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


}