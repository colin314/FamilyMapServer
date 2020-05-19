package DataAccess;

import java.lang.InternalError;
import java.lang.UnsupportedOperationException;

/**
 * Responsible for running "DELETE FROM" queries on the database
 */
public class DataDeleter {
    public DataDeleter() {

    }

    /**
     * Runs "DELETE FROM Users" and returns the number of rows affected.
     * @return The number of rows that were deleted
     * @exception InternalError if the delete fails due to foreign key constraints.
     */
    public int ClearUsers() {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

    /**
     * Runs "DELETE FROM AuthTokens" and returns the number of rows affected.
     * @return The number of rows that were deleted.
     * @exception InternalError if the delete fails due to foreign key constraints.
     */
    public int ClearTokens() {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

    /**
     * Runs "DELETE FROM Persons" and returns the number of rows affected.
     * @return The number of rows that were deleted.
     * @exception InternalError if the delete fails due to foreign key constraints.
     */
    public int ClearPersons() {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

    /**
     * Runs "DELETE FROM Events" and returns the number of rows affected.
     * @return The number of rows that were deleted.
     * @exception InternalError if the delete fails due to foreign key constraints.
     */
    public int ClearEvents() {
        throw new UnsupportedOperationException("This has not been implemented yet.");
    }

}
