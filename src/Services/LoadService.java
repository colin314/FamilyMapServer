package Services;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.FamilyMapException;
import Result.Response;
import com.google.gson.internal.$Gson$Types;

import javax.xml.crypto.Data;
import java.sql.Connection;

/**
 * Responsible for handling load requests.
 */
public class LoadService extends Service {
    UserDAO userDAO;
    PersonDAO personDAO;
    EventDAO eventDAO;
    AuthTokenDAO authTokenDAO;
    ClearService clearService;

    public LoadService() throws FamilyMapException {
        super();
        try {
            userDAO = new UserDAO(db.getConnection());
            personDAO = new PersonDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            authTokenDAO = new AuthTokenDAO(db.getConnection());
            clearService = new ClearService(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
    }

    public LoadService(Connection conn) throws FamilyMapException {
        super(conn);
        userDAO = new UserDAO(conn);
        personDAO = new PersonDAO(conn);
        eventDAO = new EventDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
        clearService = new ClearService(conn);
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
    public Response loadData(LoadRequest request) throws FamilyMapException {
        clearService.clearDatabase();
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
                throw new FamilyMapException(ex.getMessage());
            }
        }
        for (Person person : request.persons) {
            personCount++;
            try {
                personDAO.insert(person);
            }
            catch (DataAccessException ex) {
                closeConnection(false);
                throw new FamilyMapException(ex.getMessage());
            }
        }
        for (Event event : request.events) {
            eventCount++;
            try {
                eventDAO.insert(event);
            }
            catch (DataAccessException ex) {
                closeConnection(false);
                throw new FamilyMapException(ex.getMessage());
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Successfully added ");
        builder.append(personCount);
        builder.append(" persons and ");
        builder.append(eventCount);
        builder.append(" events to the database.");
        var rv = new Response(builder.toString(), true);
        closeConnection(true);
        return rv;
    }

}
