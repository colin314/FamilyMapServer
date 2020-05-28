package Request;

/**
 * Request for user to log in.
 */
public class LoginRequest {
    public LoginRequest() {}

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * The user name of the user logging in
     */
    public String userName;
    /**
     * The password for the log in attempt
     */
    public String password;
}
