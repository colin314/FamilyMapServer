package Model;

public class AuthToken {
    public AuthToken() {}

    public AuthToken(String token, String username) {
        this.token = token;
        this.username = username;
    }

    /**
     * The authorization token
     */
    public String token;
    /**
     * The username of the user that this authorization token belongs to.
     */
    public String username;
}
