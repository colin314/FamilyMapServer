package Request;

/**
 * Request to register new user.
 */
public class RegisterRequest {
    public RegisterRequest() {}

    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * User name of the user who is registering
     */
    public String userName;
    /**
     * The password for the new user account
     */
    public String password;
    /**
     * The email associated with the new user account
     */
    public String email;
    /**
     * The first name of the user
     */
    public String firstName;
    /**
     * The last name of the user
     */
    public String lastName;
    /**
     * The gender of the user
     */
    public String gender;
}
