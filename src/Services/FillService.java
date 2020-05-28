package Services;

import Result.Response;
import java.lang.UnsupportedOperationException;
import java.sql.Connection;

/**
 * Responsible for executing fill requests.
 */
public class FillService extends Service{
    public FillService() {
        super();
    }

    public FillService(Connection conn) {
        super(conn);
    }



    /**
     * Overload of {@code fillDatabase(String username, int generations)} that puts in
     * default value of 4 for {@code generations} parameter.
     * @param username The username to excute the fill for.
     * @return A Response object detailing how many persons and events were added to
     * the database.
     * @exception Response if the username is invalid.
     * @exception Response if there was an Internal server error.
     */
    public Response fillDatabase(String username) throws Response {
        return fillDatabase(username, 4);
    }

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server.
     * If there is any data in the database already associated with the given user name, it is
     * deleted. The optional "generations" parameter lets the caller specify the number of
     * generations of ancestors to be generated, and must be a non-negative integer (the
     * default is 4, which results in 31 new persons each with associated events).
     * @param username The username to execute the fill for.
     * @param generations The number of generations to generate.
     * @return A Response object detailing how many persons and events were added to
     * he database.
     * @exception Response if the username or generations parameter is invalid.
     * @exception Response if there was an Internal server error.
     */
    public Response fillDatabase(String username, int generations) throws Response {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }

}
