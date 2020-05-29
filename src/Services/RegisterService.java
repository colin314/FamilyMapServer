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

    public RegisterService(Connection conn) throws FamilyMapException {
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
        //Create User
        User newUser = new User(request);
        try {
            userDAO.insert(newUser);
            User user = userDAO.find(request.userName);
            Person newPerson = new Person(user.personID, user.userName, newUser.firstName, newUser.lastName, newUser.gender, null, null, null);
            personDAO.insert(newPerson);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        //Generate 4 generations of data

        fillService.fillDatabase(newUser.userName);
        //Generate Token
        AuthToken token = new AuthToken(UUID.randomUUID().toString(), newUser.userName);
        try {
            tokenDAO.insert(token);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        closeConnection(true);
        UserResponse response = new UserResponse(token.token, newUser.userName, newUser.personID);
        return response;
    }
}
