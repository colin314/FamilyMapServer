package DataAccess;

import DaoObjects.*;
import java.lang.InternalError;
import java.lang.UnsupportedOperationException;

/**
 * Responsible for inserting records into the database using "INSERT INTO" statements.
 */
public class DataInserter {

    /**
     * Runs the following SQL command: INSERT INTO Users VALUES []; Where [] is populated
     * using the given array.
     * @param users The users to be inserted.
     * @return The number of rows inserted into the table.
     * @exception InternalError if the insert fails due to foreign key constraints.
     * @exception InternalError if the insert fails due to unique constraints.
     * @exception InternalError if the insert fails due to check constraints
     */
    public int insertUsers(UserDao[] users) {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

    /**
     * Runs the following SQL command: INSERT INTO Persons VALUES []; Where [] is populated
     * using the given array.
     * @param persons The users to be inserted.
     * @return The number of rows inserted into the table.
     * @exception InternalError if the insert fails due to foreign key constraints.
     * @exception InternalError if the insert fails due to unique constraints.
     * @exception InternalError if the insert fails due to check constraints
     */
    public int insertPersons(PersonDao[] persons) {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

    /**
     * Runs the following SQL command: INSERT INTO Events VALUES []; Where [] is populated
     * using the given array.
     * @param events The events to be inserted.
     * @return The number of rows inserted into the table.
     * @exception InternalError if the insert fails due to foreign key constraints.
     * @exception InternalError if the insert fails due to unique constraints.
     * @exception InternalError if the insert fails due to check constraints
     */
    public int insertEvents(EventDao[] events) {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

    /**
     * Runs the following SQL command: INSERT INTO AuthTokens VALUES []; Where [] is populated
     * using the given array.
     * @param authTokens The authTokens to be inserted.
     * @return The number of rows inserted into the table.
     * @exception InternalError if the insert fails due to foreign key constraints.
     * @exception InternalError if the insert fails due to unique constraints.
     * @exception InternalError if the insert fails due to check constraints
     */
    public int insertAuthTokens(AuthTokenDao[] authTokens) {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }
}
