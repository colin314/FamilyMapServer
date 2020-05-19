package Services;

import Request.LoadRequest;
import Result.Response;

/**
 * Responsible for handling load requests.
 */
public class LoadService {
    public LoadService() {}

    /**
     *  Clears all data from the database (just like the /clear API),
     *  and then loads the posted user, person, and event data into the database.
     * @param request The request with all the data to be loaded into the database.
     * @return A Response object detailing how many users, persons, and events were added
     * to the database
     * @exception Response if the request data is invalid (missing values, invalid values, etc.)
     * @exception Response if there was an Internal server error
     */
    public Response loadData(LoadRequest request) throws Response {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }

}
