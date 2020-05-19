package Services;

import Result.Response;
import java.lang.UnsupportedOperationException;
/**
 * Responsible for clearing the database
 */
public class ClearService {
    public ClearService() {}

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     * @return A Response object stating that the operation was successful.
     * @exception Result.ErrorResponse if there was an Internal server error.
     */
    public Response clearDatabase(){
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }
}
