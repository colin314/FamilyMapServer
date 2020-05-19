package Requests;

import DataModels.User;
import DataModels.Event;
import DataModels.Person;

public class LoadRequest {
    LoadRequest() {}

    public User[] users;
    public Person[] persons;
    public Event[] events;

}