package DaoObjects;

public class AuthTokenDao {
    AuthTokenDao () {}

    public AuthTokenDao(String token, String username) {
        Token = token;
        Username = username;
    }

    /**
     * The authorization token
     */
    public String Token;
    /**
     * The username of the user that this authorization token belongs to.
     */
    public String Username;
}
