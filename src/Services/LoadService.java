package Services;

import DataAccess.DataAccessException;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.Response;

import java.sql.Connection;

/**
 * Responsible for handling load requests.
 */
public class LoadService extends ClearService {
    public LoadService() throws Response {
        super();
    }

    public LoadService(Connection conn) throws Response {
        super(conn);
    }

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
        clearDatabase();
        int userCount = 0;
        int personCount = 0;
        int eventCount = 0;
        for (User user : request.users) {
            userCount++;
            try {
                userDAO.insert(user);
            }
            catch (DataAccessException ex) {
                closeConnection(false);
                throw new Response(ex.getMessage(), false);
            }
        }
        for (Person person : request.persons) {
            personCount++;
            try {
                personDAO.insert(person);
            }
            catch (DataAccessException ex) {
                closeConnection(false);
                throw new Response(ex.getMessage(), false);
            }
        }
        for (Event event : request.events) {
            eventCount++;
            try {
                eventDAO.insert(event);
            }
            catch (DataAccessException ex) {
                closeConnection(false);
                throw new Response(ex.getMessage(), false);
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Successfully added ");
        builder.append(personCount);
        builder.append(" persons and ");
        builder.append(eventCount);
        builder.append(" events to the database.");
        Response rv = new Response(builder.toString(), true);
        closeConnection(true);
        return rv;
    }

}
