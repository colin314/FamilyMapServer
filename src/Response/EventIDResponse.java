package Response;

import DataModels.Event;
import com.sun.java.accessibility.util.EventID;

public class EventIDResponse extends Event {

    public EventIDResponse(Event event) {
        super(event);
        success = true;
    }

    boolean success;

}
