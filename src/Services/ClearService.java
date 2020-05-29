package Services;

import DataAccess.*;
import Result.FamilyMapException;

import java.sql.Connection;

/**
 * Responsible for clearing the database
 */
public class ClearService extends Service {
    public ClearService() throws FamilyMapException {
        super();
        try {
            userDAO = new UserDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            personDAO = new PersonDAO(db.getConnection());
            authTokenDAO = new AuthTokenDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage(), false);
        }
    }

    public ClearService(Connection conn) throws FamilyMapException {
        super(conn);
        userDAO = new UserDAO(conn);
        eventDAO = new EventDAO(conn);
        personDAO = new PersonDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
    }
    Database db;
    UserDAO userDAO;
    EventDAO eventDAO;
    PersonDAO personDAO;
    AuthTokenDAO authTokenDAO;

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     * @return A Response object stating that the operation was successful.
     * @exception FamilyMapException if there was an Internal server error.
     */
    public FamilyMapException clearDatabase() throws FamilyMapException {
        try {
            userDAO.clear();
            eventDAO.clear();
            personDAO.clear();
            authTokenDAO.clear();
        }
        catch (DataAccessException ex) {
            if (db != null) {
                closeConnection(false);
            }
            throw new FamilyMapException(ex.getMessage(),false);
        }
        closeConnection(true);
        return new FamilyMapException("Clear succeeded.", true);
    }
}
