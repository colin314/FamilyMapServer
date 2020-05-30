package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.RegisterRequest;
import Result.FamilyMapException;
import Result.UserResponse;

import java.sql.Connection;
import java.util.UUID;

/**
 * Responsible for registering new users.
 */
public class RegisterService extends Service {
    UserDAO userDAO;
    PersonDAO personDAO;
    AuthTokenDAO tokenDAO;
    FillService fillService;

    /**
     * Creates a connection to the database and opens it.
     * @throws FamilyMapException if there is a problem opening the connections (bad login, bad server address
     * etc.)
     */
    public RegisterService() throws FamilyMapException {
        super();
        try {
            Connection conn = db.getConnection();
            userDAO =  new UserDAO(conn);
            tokenDAO = new AuthTokenDAO(db.getConnection());
            personDAO = new PersonDAO(db.getConnection());
            fillService = new FillService(db.getConnection());
        }
        catch(DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
    }

    /**
     * Allows an existing connection to be used in this service.
     * @param conn An existing open database connection
     */
    public RegisterService(Connection conn) {
        super(conn);
        db = null;
        userDAO =  new UserDAO(conn);
        tokenDAO = new AuthTokenDAO(conn);
        personDAO = new PersonDAO(conn);
        fillService = new FillService(conn);
    }

    /**
     * Registers the given user
     * @param request The incoming register request
     * @return A UserResponse object with details about the user (authToken, userName, and personID)
     * indicating the register request was successful.
     * @exception FamilyMapException if the username is already taken
     * @exception FamilyMapException if a required property is missing or has an invalid value
     * @exception FamilyMapException if there was an Internal server error.
     */
    public UserResponse registerUser(RegisterRequest request) throws FamilyMapException {
        User newUser = new User(request);
        try {
            userDAO.insert(newUser);
            var user = userDAO.find(request.userName);
            var newPerson = new Person(user.getPersonID(), user.getUserName(), newUser.getFirstName(),
                    newUser.getLastName(), newUser.getGender(), null, null, null);
            personDAO.insert(newPerson);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            if (ex.getMessage().contains("Violation of UNIQUE KEY")) {
                throw new FamilyMapException("Username already exists. Cannot register new user under that username.");
            } else {
                throw new FamilyMapException(ex.getMessage());
            }
        }

        fillService.fillDatabase(newUser.getUserName());

        AuthToken token = new AuthToken(UUID.randomUUID().toString(), newUser.getUserName());
        try {
            tokenDAO.insert(token);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        closeConnection(true);
        UserResponse response = new UserResponse(token.token, newUser.getUserName(), newUser.getPersonID());
        return response;
    }
}
