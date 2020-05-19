package Response;

import DataModels.Event;
import java.util.ArrayList;
import java.util.List;

public class EventResponse {
    public EventResponse(List<Event> events) {
        success = true;
        data = new ArrayList<Event>();
        for (Event event : events) {
            data.add(new Event(event));
        }
    }

    public ArrayList<Event> data;
    public boolean success;
}
