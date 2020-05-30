package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.Event;
import Result.FamilyMapException;
import Result.EventIDResponse;
import Result.EventResponse;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Responsible for handling requests for events.
 */
public class EventService extends AuthService{
    public EventService() throws FamilyMapException {
        super();
        try {
            eventDAO = new EventDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
    }

    public EventService(Connection conn) throws FamilyMapException {
        super(conn);
        eventDAO = new EventDAO(conn);
    }
    EventDAO eventDAO;

    /**
     * Returns the single Event object with the specified ID.
     * @param eventID the ID of the event requested.
     * @param authToken The authToken for the user making the request.
     * @return An EventIDResponse object, containing the details of the requested event.
     * @exception FamilyMapException if the auth token is invalid.
     * @exception FamilyMapException if the eventID is invalid.
     * @exception FamilyMapException if the requested event does not belong to this user.
     * @exception FamilyMapException if there is an Internal server error.
     */
    public EventIDResponse getEventByID(String eventID, String authToken) throws FamilyMapException, UnauthorizedException {
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
        EventIDResponse response = null;
        try {
            Event event = eventDAO.find(eventID);
            if (event == null) {
                throw new FamilyMapException("No event found for the given EventID");
            }
            response = new EventIDResponse(event);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        closeConnection(true);
        if (!response.associatedUsername.equals(userName)) {
            throw new FamilyMapException("Requested event does not belong to this user");
        }
        return response;
    }

    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     * @param authToken The authToken for the user making the request.
     * @return An EventResponse object, containing an array of Event objects of the events that
     * are owned by the current user.
     * @exception FamilyMapException if the auth token is invalid.
     * @exception FamilyMapException if there is an Internal server error.
     */
    public EventResponse getEventByUser(String authToken) throws FamilyMapException, UnauthorizedException {
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
        EventResponse response = null;
        try {
            ArrayList<Event> events = eventDAO.findByUser(userName);
            response = new EventResponse(events);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        closeConnection(true);
        return response;
    }
}
