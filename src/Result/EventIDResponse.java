package Result;

import Model.Event;

public class EventIDResponse extends Event {

    public EventIDResponse(Event event) {
        super(event);
        success = true;
    }

    boolean success;

}
