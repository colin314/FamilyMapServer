package UnitTests;

import DataAccess.*;
import Request.RegisterRequest;
import Result.FamilyMapException;
import Result.UserResponse;
import Services.RegisterService;
import org.junit.jupiter.api.*;
import java.sql.*;

public class RegisterServiceTest {
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
    @DisplayName("User successfully registered.")
    void RegisterSuccessful() {
        RegisterRequest request = new RegisterRequest("neilO", "password", "neil@noanderson.com",
                "Neil", "Anderson", "m");
        RegisterService service = null;
        UserResponse actual = null;
        try{
            service = new RegisterService(conn);
            actual = service.registerUser(request);
        }
        catch (FamilyMapException ex) {
            throw new AssertionError(ex.getMessage());
        }
        Assertions.assertTrue(actual.userName.equalsIgnoreCase("neilO"));

        String user = null;
        ResultSet rs = null;
        String sql = "SELECT Username FROM AuthTokens WHERE Token = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, actual.authToken);
            rs = stmt.executeQuery();
            if (rs.next()){
                user = rs.getString("Username");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError(e.getMessage());
        }
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.equals("neilO"));
    }
}
