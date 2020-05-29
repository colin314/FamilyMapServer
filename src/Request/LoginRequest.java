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

    public void validate() throws BadRequest {
        if (userName == null) { throw new BadRequest("No user name was provided in the request."); }
        if (password == null) {throw new BadRequest("No password was provided in the request."); }
    }
}
