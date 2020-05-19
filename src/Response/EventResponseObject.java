package Response;

import DataModels.Event;

public class EventResponseObject {
    public EventResponseObject(Event event) {
        eventID = event.eventID;
        personID = event.personID;
        associatedUsername = event.userName;
        latitude = event.latitude;
        longitude = event.longitude;
        country = event.country;
        city = event.city;
        eventType = event.EventType;
        year = event.year;
    }

    public String eventID;
    public String personID;
    public String associatedUsername;
    public double latitude;
    public double longitude;
    public String country;
    public String city;
    public String eventType;
    public int year;

}
