package Request;

import Result.FamilyMapException;

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

    public void validateRequest() throws BadRequest {
        if (userName == null) { throw new BadRequest("No username provided in the request"); }
        if (password == null) {throw new BadRequest("No password provided in the request"); }
        if (email == null) { throw new BadRequest("No username provided in the request"); }
        if (firstName == null) { throw new BadRequest("No first name provided in the request"); }
        if (lastName == null) { throw new BadRequest("No last name provided in the request"); }
        if (gender == null) { throw new BadRequest("No gender provided in the request"); }
        if (!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) {
            throw new BadRequest("Invalid gender in request. Gender must be either 'm' or 'f' (case-insensitive)");
        }
    }
}
