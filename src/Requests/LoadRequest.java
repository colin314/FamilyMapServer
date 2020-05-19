package Requests;

import DataModels.User;
import DataModels.Event;
import DataModels.Person;

/**
 * Request to clear database and then load data to database
 */
public class LoadRequest {
    LoadRequest() {}

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