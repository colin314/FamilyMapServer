package Model;

import DaoObjects.AuthTokenDao;

public class AuthToken {
    public AuthToken() {}

    public AuthToken(AuthTokenDao authToken) {
        token = authToken.Token;
        username = authToken.Username;
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
