package Response;

import DataModels.Event;
import java.util.ArrayList;
import java.util.List;

public class EventResponse {
    public EventResponse(List<Event> events) {
        success = true;
        data = new ArrayList<EventResponseObject>();
        for (Event event : events) {
            data.add(new EventResponseObject(event));
        }
    }

    public ArrayList<EventResponseObject> data;
    public boolean success;
}
