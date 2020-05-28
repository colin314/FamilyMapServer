package Model;

import Result.EventIDResponse;

public class Event {
    public Event() {}

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

    public Event(String eventID, String associatedUsername, String personID, double latitude, double longitude,
                 String country, String city, String eventType, int year) {
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

    public String eventID;
    public String associatedUsername;
    public String personID;
    public double latitude;
    public double longitude;
    public String country;
    public String city;
    public String eventType;
    public int year;

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Event.class || obj.getClass() == EventIDResponse.class) {
            Event that = (Event)obj;
            if (!this.eventID.equals(that.eventID)) { return false; }
            if (!this.associatedUsername.equals(that.associatedUsername)) { return false; }
            if (!this.personID.equals(that.personID)) { return false; }
            if (!this.country.equals(that.country)) { return false; }
            if (!this.city.equals(that.city)) { return false; }
            if (!this.eventType.equalsIgnoreCase(that.eventType)) { return false; }
            if (this.year != that.year) {return false;}
        }
        else {
            return false;
        }
        return true;

    }
}
