package DataModels;

import DaoObjects.EventDao;

public class Event {
    Event() {}

    Event(EventDao event) {
        eventID = event.Event_ID;
        userName = event.Username;
        personID = event.Person_ID;
        latitude = event.Latitude;
        longitude = event.Longitude;
        country = event.Country;
        city = event.City;
        EventType = event.EventType;
        year = event.Event_Year;
    }

    public String eventID;
    public String userName;
    public String personID;
    public double latitude;
    public double longitude;
    public String country;
    public String city;
    public String EventType;
    public int year;
}
