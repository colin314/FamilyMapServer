package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.Person;
import Result.*;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Responsible for handling Person requests
 */
public class PersonService extends AuthService {
    public PersonService() throws FamilyMapException {
        super();
        try {
            personDAO = new PersonDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
    }

    public PersonService(Connection conn) throws FamilyMapException {
        super(conn);
        personDAO = new PersonDAO(conn);
    }
    PersonDAO personDAO;

    /**
     * Returns the single Person object with the specified ID.
     * @param personID The person ID of the person to retrieve from the database.
     * @param authToken The authToken for the current user
     * @return A PersonIDResponse containing the person's details.
     * @exception FamilyMapException if the auth token is invalid.
     * @exception FamilyMapException if the personID is invalid.
     * @exception FamilyMapException if the requested person does not belong to this user.
     * @exception FamilyMapException if there is an Internal server error.
     */
    public PersonIDResponse getPersonByID(String personID, String authToken) throws FamilyMapException, UnauthorizedException {
        String userName = null;
        try {
            userName = verifyToken(authToken);
        }
        catch (FamilyMapException r) {
            closeConnection(false);
            throw r;
        }
        if (userName == null) {
            closeConnection(false);
            throw new UnauthorizedException("Invalid auth token");
        }

        //Get Event
        PersonIDResponse response = null;
        try {
            Person person = personDAO.find(personID);
            response = new PersonIDResponse(person);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        closeConnection(true);
        if (!response.associatedUsername.equalsIgnoreCase(userName)) {
            throw new UnauthorizedException("Requested person does not belong to this user");
        }
        return response;
    }

    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     * @param authToken The authToken for the current user
     * @return A PersonResponse object containing Person objects that correspond to the given authToken.
     * @exception FamilyMapException if the auth token is invalid.
     * @exception FamilyMapException if there is an Internal server error.
     */
    public PersonResponse getPersonByUsername(String authToken) throws FamilyMapException, UnauthorizedException {
        String userName = null;
        try {
            userName = verifyToken(authToken);
        }
        catch (FamilyMapException r) {
            closeConnection(false);
            throw r;
        }
        if (userName == null) {
            closeConnection(false);
            throw new UnauthorizedException("Invalid auth token");
        }
        PersonResponse response = null;
        try {
            ArrayList<Person> persons = personDAO.findByUser(userName);
            response = new PersonResponse(persons);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        closeConnection(true);
        return response;
    }

}
