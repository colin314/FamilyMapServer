package Services;

import DataAccess.DataAccessException;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.FamilyMapException;

import java.sql.Connection;

/**
 * Responsible for handling load requests.
 */
public class LoadService extends ClearService {
    public LoadService() throws FamilyMapException {
        super();
    }

    public LoadService(Connection conn) throws FamilyMapException {
        super(conn);
    }

    /**
     *  Clears all data from the database (just like the /clear API),
     *  and then loads the posted user, person, and event data into the database.
     * @param request The request with all the data to be loaded into the database.
     * @return A Response object detailing how many users, persons, and events were added
     * to the database
     * @exception FamilyMapException if the request data is invalid (missing values, invalid values, etc.)
     * @exception FamilyMapException if there was an Internal server error
     */
    public FamilyMapException loadData(LoadRequest request) throws FamilyMapException {
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
                throw new FamilyMapException(ex.getMessage(), false);
            }
        }
        for (Person person : request.persons) {
            personCount++;
            try {
                personDAO.insert(person);
            }
            catch (DataAccessException ex) {
                closeConnection(false);
                throw new FamilyMapException(ex.getMessage(), false);
            }
        }
        for (Event event : request.events) {
            eventCount++;
            try {
                eventDAO.insert(event);
            }
            catch (DataAccessException ex) {
                closeConnection(false);
                throw new FamilyMapException(ex.getMessage(), false);
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Successfully added ");
        builder.append(personCount);
        builder.append(" persons and ");
        builder.append(eventCount);
        builder.append(" events to the database.");
        FamilyMapException rv = new FamilyMapException(builder.toString(), true);
        closeConnection(true);
        return rv;
    }

}
