package Result;

/**
 * Response that returns user information (authentication token, user name, and person ID) along with the success code.
 */
public class UserResponse {
    /**
     * Constructor for success responses. Success field is automatically set to true with this constructor.
     * @param authToken Non-empty auth token string
     * @param userName User name passed in with request
     * @param personID Non-empty string containing the Person ID of the user’s generated Person object
     */
    UserResponse(String authToken, String userName, String personID) {
        success = true;
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    /**
     * Authentication token string
     */
    public String authToken;
    /**
     * User name passed in with request
     */
    public String userName;
    /**
     * Person ID of the user's generated person object
     */
    public String personID;
    /**
     * Whether or not the request was successful
     */
    boolean success;
}
