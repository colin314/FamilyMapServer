package Request;

import Model.User;
import Model.Event;
import Model.Person;

/**
 * Request to clear database and then load data to database
 */
public class LoadRequest {
    LoadRequest() {}

    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    /**
     * Array of users to load into database
     */
    public User[] users;
    /**
     * Array of persons to load into database
     */
    public Person[] persons;
    /**
     * Array of events to load into database
     */
    public Event[] events;

}