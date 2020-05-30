package UnitTests;

import DataAccess.*;
import Model.*;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

public class AuthTokenDAOTest{
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
    @DisplayName("Insert AuthToken successfully inserts")
    void insert_success() {
        String id = "newID";
        AuthToken token = new AuthToken(id, "colin314");
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        Assertions.assertDoesNotThrow(() -> authTokenDAO.insert(token));
        Assertions.assertDoesNotThrow(() -> {
            ResultSet rs = conn.prepareStatement("SELECT * FROM AuthTokens WHERE Token = 'newID'")
                    .executeQuery();
            Assertions.assertTrue(rs.next(), "The person was not actually inserted into the database");
            rs.close();
        });
    }

    @Test
    @DisplayName("Insert AuthToken duplicate fails")
    void insert_duplicateFails() {
        String id = "c0eb66bb-8ff9-4131-bef9-6090100dc1c2";
        AuthToken token = new AuthToken(id, "colin314");
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        Assertions.assertThrows(DataAccessException.class, () -> authTokenDAO.insert(token));
        try {
            authTokenDAO.insert(token);
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("Violation of UNIQUE KEY constraint 'UQ__AuthToke__1EB4F817EF646E3C'. " +
                            "Cannot insert duplicate key in object 'dbo.AuthTokens'. The duplicate key value is " +
                            "(c0eb66bb-8ff9-4131-bef9-6090100dc1c2).",
                    ex.getMessage());
        }
    }

    @Test
    @DisplayName("Find user by token finds correct user")
    void findUser_Successful() {
        String expected = "colin314";
        String actual = null;
        try {
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            actual = authTokenDAO.findUser("c0eb66bb-8ff9-4131-bef9-6090100dc1c2");
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            throw new AssertionError("PersonDAO.find() threw an error");
        }
        Assertions.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Find user for invalid token returns null")
    void findUser_BadTokenReturnsNull() {
        try {
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            String foundUser = authTokenDAO.findUser("badToken");
            Assertions.assertNull(foundUser);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            throw new AssertionError("PersonDAO.find() threw an error");
        }

    }
}
