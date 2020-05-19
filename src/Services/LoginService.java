package Services;

import Request.LoginRequest;
import Result.UserResponse;
import Result.Response;
import java.lang.UnsupportedOperationException;

/**
 * Responsible for attempting to log users in and returning an auth token if
 * successful.
 */
public class LoginService {
    public LoginService() {}

    /**
     * Attempts to log the user in using the given username and password.
     * @param request The request, which contains the username and password.
     * @return A UserResponse object with the authToken for the current log-in session.
     * @exception Response if username or password is missing or invalid
     * @exception Response if there was an Internal server error.
     */
    public UserResponse loginUser(LoginRequest request) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }
}
