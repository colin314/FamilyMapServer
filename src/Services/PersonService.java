package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.Event;
import Model.Person;
import Result.*;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Responsible for handling Person requests
 */
public class PersonService extends AuthService {
    Database db;
    public PersonService() throws Response {
        super();
        try {
            personDAO = new PersonDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new Response(ex.getMessage(), false);
        }
    }

    public PersonService(Connection conn) throws Response {
        super(conn);
        personDAO = new PersonDAO(conn);
    }
    PersonDAO personDAO;

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
    public PersonIDResponse getPersonByID(String personID, String authToken) throws Response {
        String userName = null;
        try {
            userName = verifyToken(authToken);
        }
        catch (Response r) {
            closeConnection(false);
            throw r;
        }
        if (userName == null) {
            closeConnection(false);
            throw new Response("Invalid auth token", false);
        }

        //Get Event
        PersonIDResponse response = null;
        try {
            Person person = personDAO.find(personID);
            response = new PersonIDResponse(person);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new Response(ex.getMessage(), false);
        }
        closeConnection(true);
        if (!response.associatedUsername.equalsIgnoreCase(userName)) {
            throw new Response("Requested person does not belong to this user", false);
        }
        return response;
    }

    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     * @param authToken The authToken for the current user
     * @return A PersonResponse object containing Person objects that correspond to the given authToken.
     * @exception Response if the auth token is invalid.
     * @exception Response if there is an Internal server error.
     */
    public PersonResponse getPersonByUsername(String authToken) throws Response {
        String userName = null;
        try {
            userName = verifyToken(authToken);
        }
        catch (Response r) {
            closeConnection(false);
            throw r;
        }
        if (userName == null) {
            closeConnection(false);
            throw new Response("Invalid auth token", false);
        }
        PersonResponse response = null;
        try {
            ArrayList<Person> persons = personDAO.findByUser(userName);
            response = new PersonResponse(persons);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new Response(ex.getMessage(), false);
        }
        closeConnection(true);
        return response;
    }

}
