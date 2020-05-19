package DataAccess;

import DaoObjects.*;
import java.lang.UnsupportedOperationException;
import java.lang.InternalError;

/**
 * Class that is responsible for running SELECT queries and returning the results. Each
 * method in the class basically represents one query
 */
public class DataAccessor {
    public DataAccessor() {}

    /**
     * Runs the this query: SELECT * FROM Users WHERE Username = @username; Where @username
     * is the username passed into the function.
     * @param username The username to query the database for
     * @return The result of the query in a User object.
     * @exception InternalError if more than one record with the given username is found.
     */
    public UserDao GetUser(String username) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }

    /**
     * Runs this query: SELECT * FROM Persons WHERE Person_ID = @personID; Where @personID
     * is the person ID passed into the function.
     * @param personID The ID of the person
     * @return The result of the query in a PersonDao object.
     * @exception InternalError if more than one person with the given person ID is found.
     */
    public PersonDao GetPerson(String personID) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }

    /**
     * Runs this query: SELECT * FROM Events WHERE Event_ID = @eventID; Where @eventID
     * is the event ID passed into the function.
     * @param eventID The ID of the event
     * @return The result of the query in an EventDao object.
     * @exception Result.ErrorResponse more than one even with the given event ID is found.
     */
    public EventDao GetEvent(String eventID) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }

    /**
     * Runs this query: SELECT p.* FROM Users u LEFT JOIN Persons p ON u.Username = p.Username WHERE
     * u.Username = @username; Where @username is the username passed into the function.
     * @param username Username of the user
     * @return The result of the query in an array of Person objects.
     */
    public PersonDao[] GetPersonsByUser(String username) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }

    /**
     * Runs this query: SELECT e.* FROM Users u LEFT JOIN Events e ON u.Username = e.Username WHERE
     * u.Username = @username; Where @username is the username passed into the function.
     * @param username Username of the user
     * @return The result of the query in an array of Event objects.
     */
    public EventDao[] GetEventsByUser(String username) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }

    /**
     * Runs this query: SELECT * FROM AuthTokens WHERE Token = @authToken; Where @authToken
     * is the token passed into the function.
     * @param authToken The authorization token
     * @return An AuthTokenDao object that corresponds to the given authorization token
     * @exception InternalError if the provided authToken corresponds to more than one record.
     * @exception InternalError if the provided authToken doesn't correspond to any records.
     */
    public AuthTokenDao GetUserByToken(String authToken) {
        throw new UnsupportedOperationException("This has not been implemented yet");
    }


}
