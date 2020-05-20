package UnitTests;

import DataAccess.*;
import Model.*;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.*;

public class UserDAOTest {
    private Connection conn = null;
    private UserDAO userDAO;
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
            userDAO = new UserDAO(conn);
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

    //Good insert
    @Test
    @DisplayName("New User successfully inserted")
    void UserInsert_NewUser_Successful() {
        String newPersonID = UUID.randomUUID().toString();
        User newUser = new User("noanderson","password", "neil@noanderson.com", "Neil",
                "Anderson", "m", newPersonID);
        Assertions.assertDoesNotThrow(() -> userDAO.insert(newUser));
        try(PreparedStatement stmnt = conn.prepareStatement(String.format("SELECT * FROM " +
                "Users u LEFT JOIN Persons p ON u.Person_ID = p.Person_ID WHERE u.Person_ID = '%s'",newPersonID))) {
            ResultSet rs = stmnt.executeQuery();
            Assertions.assertTrue(rs.next(), "No record was found after insert.");
            User foundUser = new User(rs.getString("Username"), rs.getString("User_Password"),
                    rs.getString("Email"), rs.getString("First_Name"), rs.getString("Last_Name"),
                    rs.getString("Gender"), rs.getString("Person_ID"));
            Assertions.assertTrue(newUser.fullEquals(foundUser), "The user that was found in the database did not match the user inserted.");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error in checking if record was successfully inserted");
        }
    }

    @Test
    @DisplayName("New User insert fails when Username is taken (case-insensitive)")
    void UserInsert_DuplicateUsername_Fails() {
        User newUser = new User("COLIN314", "password", "email@gmail.com",
                "Colin", "Anderson", "m", UUID.randomUUID().toString());
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.insert(newUser));
    }

    @Test
    @DisplayName("Find user returns expected object")
    void UserFind_ValidUsername_ReturnsExpected() {
        User expectedUser = new User("kDavis", "password", "kye@gmail.com",
                "Kye", "Davis", "m", "D8B340FC-F421-4472-8D08-FB28D3D5138F");
        User actualUser = null;
        try {
            actualUser = userDAO.find(expectedUser.userName);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            throw new AssertionError("UserDAO.find() threw an error");
        }
        Assert.assertTrue(expectedUser.fullEquals(actualUser));
    }

    @Test
    @DisplayName("Find user with invalid username returns null")
    void UserFind_InvalidUsername_ReturnsNull() {
        User foundUser = null;
        try {
            foundUser = userDAO.find("notARealUser");
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new AssertionError("UserDAO.find() threw and error");
        }
        Assertions.assertNull(foundUser);
    }

    @Test
    @DisplayName("Clearing users removes all users and the data associated with the users")
    void UserClear_SuccessfullyRemovesData() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users")){
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            Assertions.assertTrue(rowCount > 0, "The initial state of the test DB is incorrect.");
            conn.rollback();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError("Error thrown when attempting to verify clear statement.");
        }
        Assertions.assertDoesNotThrow(() -> userDAO.clear());
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Persons")){
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
    }

}
