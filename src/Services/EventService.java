package Services;

import Result.Response;
import Result.EventIDResponse;
import Result.EventResponse;

/**
 * Responsible for handling requests for events.
 */
public class EventService {
    public EventService() {}

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
        throw new UnsupportedOperationException("Not implemented");
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
        throw new UnsupportedOperationException("Not implemented");
    }
}
