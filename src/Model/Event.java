package Model;

import DaoObjects.EventDao;

public class Event {
    Event() {}

    public Event(Event event) {
        this.eventID = event.eventID;
        this.associatedUsername = event.associatedUsername;
        this.personID = event.personID;
        this.latitude = event.latitude;
        this.longitude = event.longitude;
        this.country = event.country;
        this.city = event.city;
        this.eventType = event.eventType;
        this.year = event.year;
    }

    public Event(String eventID, String associatedUsername, String personID, double latitude, double longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    Event(EventDao event) {
        eventID = event.Event_ID;
        associatedUsername = event.Username;
        personID = event.Person_ID;
        latitude = event.Latitude;
        longitude = event.Longitude;
        country = event.Country;
        city = event.City;
        eventType = event.EventType;
        year = event.Event_Year;
    }

    public String eventID;
    public String associatedUsername;
    public String personID;
    public double latitude;
    public double longitude;
    public String country;
    public String city;
    public String eventType;
    public int year;
}
