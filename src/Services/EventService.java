package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.Event;
import Result.Response;
import Result.EventIDResponse;
import Result.EventResponse;

import javax.xml.crypto.Data;
import java.lang.annotation.Repeatable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for handling requests for events.
 */
public class EventService extends AuthService{
    Database db;
    public EventService() throws Response {
        super();
        try {
            eventDAO = new EventDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new Response(ex.getMessage(), false);
        }
    }

    public EventService(Connection conn) throws Response {
        super(conn);
        eventDAO = new EventDAO(conn);
    }
    EventDAO eventDAO;

    /**
     * Returns the single Event object with the specified ID.
     * @param eventID the ID of the event requested.
     * @param authToken The authToken for the user making the request.
     * @return An EventIDResponse object, containing the details of the requested event.
     * @exception Response if the auth token is invalid.
     * @exception Response if the eventID is invalid.
     * @exception Response if the requested event does not belong to this user.
     * @exception Response if there is an Internal server error.
     */
    public EventIDResponse getEventByID(String eventID, String authToken) throws Response {
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
        EventIDResponse response = null;
        try {
            Event event = eventDAO.find(eventID);
            response = new EventIDResponse(event);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new Response(ex.getMessage(), false);
        }
        closeConnection(true);
        if (!response.associatedUsername.equals(userName)) {
            throw new Response("Requested event does not belong to this user", false);
        }
        return response;
    }

    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     * @param authToken The authToken for the user making the request.
     * @return An EventResponse object, containing an array of Event objects of the events that
     * are owned by the current user.
     * @exception Response if the auth token is invalid.
     * @exception Response if there is an Internal server error.
     */
    public EventResponse getEventByUser(String authToken) throws Response {
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
        EventResponse response = null;
        try {
            ArrayList<Event> events = eventDAO.findByUser(userName);
            response = new EventResponse(events);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new Response(ex.getMessage(), false);
        }
        closeConnection(true);
        return response;
    }
}
