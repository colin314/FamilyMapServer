package Requests;

/**
 * Request for user to log in.
 */
public class LoginRequest {
    public LoginRequest() {}

    /**
     * The user name of the user logging in
     */
    public String userName;
    /**
     * The password for the log in attempt
     */
    public String password;
}
