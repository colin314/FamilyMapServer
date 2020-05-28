package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.UserResponse;
import Result.Response;
import java.lang.UnsupportedOperationException;
import java.sql.Connection;
import java.util.UUID;

/**
 * Responsible for attempting to log users in and returning an auth token if
 * successful.
 */
public class LoginService extends Service {
    public LoginService() throws Response {
        super();
        try {
            userDAO = new UserDAO(db.getConnection());
            authTokenDAO = new AuthTokenDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new Response(ex.getMessage(), false);
        }
    }

    public LoginService(Connection conn) {
        super(conn);
        userDAO = new UserDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
    }

    UserDAO userDAO;
    AuthTokenDAO authTokenDAO;

    /**
     * Attempts to log the user in using the given username and password.
     * @param request The request, which contains the username and password.
     * @return A UserResponse object with the authToken for the current log-in session.
     * @exception Response if username or password is missing or invalid
     * @exception Response if there was an Internal server error.
     */
    public UserResponse loginUser(LoginRequest request) throws Response {
        UserResponse response = null;
        try {
            User user = userDAO.find(request.userName);
            if (user == null) {
                throw new Response("The given username does not correspond to a known user", false);
            }
            if (!user.password.equals(request.password)) {
                throw new Response("The given username and password do not match", false);
            }
            String newToken = UUID.randomUUID().toString();
            AuthToken token = new AuthToken(newToken, user.userName);
            authTokenDAO.insert(token);
            response = new UserResponse(newToken, user.userName, user.personID);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new Response(ex.getMessage(), false);
        }
        closeConnection(true);
        return response;
    }
}
