package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Model.AuthToken;
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
    AuthTokenDAO tokenDAO;
    Database db;
    public RegisterService() throws Response {
        super();
        try {
            userDAO =  new UserDAO(db.getConnection());
            tokenDAO = new AuthTokenDAO(db.getConnection());
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
