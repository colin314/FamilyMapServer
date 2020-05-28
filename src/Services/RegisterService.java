package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.RegisterRequest;
import Result.UserResponse;
import Result.Response;

import javax.xml.crypto.Data;
import java.lang.UnsupportedOperationException;
import java.sql.Connection;
import java.util.UUID;

/**
 * Responsible for registering new users.
 */
public class RegisterService extends Service {
    UserDAO userDAO;
    PersonDAO personDAO;
    AuthTokenDAO tokenDAO;
    Database db;
    public RegisterService() throws Response {
        super();
        try {
            userDAO =  new UserDAO(db.getConnection());
            tokenDAO = new AuthTokenDAO(db.getConnection());
            personDAO = new PersonDAO(db.getConnection());
        }
        catch(DataAccessException ex) {
            throw new Response(ex.getMessage(), false);
        }
    }

    public RegisterService(Connection conn) throws Response {
        super(conn);
        db = null;
        userDAO =  new UserDAO(conn);
        tokenDAO = new AuthTokenDAO(conn);
        personDAO = new PersonDAO(conn);
    }

    /**
     * Registers the given user
     * @param request The incoming register request
     * @return A UserResponse object with details about the user (authToken, userName, and personID)
     * indicating the register request was successful.
     * @exception Response if the username is already taken
     * @exception Response if a required property is missing or has an invalid value
     * @exception Response if there was an Internal server error.
     */
    public UserResponse registerUser(RegisterRequest request) throws Response {
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
            throw new Response(ex.getMessage(),false);
        }
        //Generate Token
        AuthToken token = new AuthToken(UUID.randomUUID().toString(), newUser.userName);
        try {
            tokenDAO.insert(token);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new Response(ex.getMessage(), false);
        }
        closeConnection(true);
        UserResponse response = new UserResponse(token.token, newUser.userName, newUser.personID);
        return response;
    }
}
