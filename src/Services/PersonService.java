package Services;

import Result.PersonIDResponse;
import Result.PersonResponse;
import Result.Response;

/**
 * Responsible for handling Person requests
 */
public class PersonService {
    public PersonService() {}

    /**
     * Returns the single Person object with the specified ID.
     * @param personID The person ID of the person to retrieve from the database.
     * @param authToken The authToken for the current user
     * @return A PersonIDResponse containing the person's details.
     * @exception Response if the auth token is invalid.
     * @exception Response if the personID is invalid.
     * @exception Response if the requested person does not belong to this user.
     * @exception Response if there is an Internal server error.
     */
    public PersonIDResponse getPersonByID(String personID, String authToken) {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     * @param authToken The authToken for the current user
     * @return A PersonResponse object containing Person objects that correspond to the given authToken.
     * @exception Response if the auth token is invalid.
     * @exception Response if there is an Internal server error.
     */
    public PersonResponse getPersonByUsername(String authToken) {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

}
