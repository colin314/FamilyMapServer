package Services;

import Request.RegisterRequest;
import Result.UserResponse;
import Result.Response;
import java.lang.UnsupportedOperationException;
/**
 * Responsible for registering new users.
 */
public class RegisterService {
    public RegisterService() {}

    /**
     * Registers the given user
     * @param request The incoming register request
     * @return A UserResponse object with details about the user (authToken, userName, and personID)
     * indicating the register request was successful.
     * @exception Response if the username is already taken
     * @exception Response if a required property is missing or has an invalid value
     * @exception Response if there was an Internal server error.
     */
    public UserResponse registerUser(RegisterRequest request) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }
}
