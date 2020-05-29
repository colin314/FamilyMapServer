package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.FamilyMapException;
import Result.UserResponse;

import java.sql.Connection;
import java.util.UUID;

/**
 * Responsible for attempting to log users in and returning an auth token if
 * successful.
 */
public class LoginService extends Service {
    public LoginService() throws FamilyMapException {
        super();
        try {
            userDAO = new UserDAO(db.getConnection());
            authTokenDAO = new AuthTokenDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage(), false);
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
     * @exception FamilyMapException if username or password is missing or invalid
     * @exception FamilyMapException if there was an Internal server error.
     */
    public UserResponse loginUser(LoginRequest request) throws FamilyMapException, UnauthorizedException {
        UserResponse response = null;
        try {
            User user = userDAO.find(request.userName);
            if (user == null) {
                throw new UnauthorizedException("The given username does not correspond to a known user");
            }
            if (!user.password.equals(request.password)) {
                throw new UnauthorizedException("The given username and password do not match");
            }
            String newToken = UUID.randomUUID().toString();
            AuthToken token = new AuthToken(newToken, user.userName);
            authTokenDAO.insert(token);
            response = new UserResponse(newToken, user.userName, user.personID);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage(), false);
        }
        closeConnection(true);
        return response;
    }
}
