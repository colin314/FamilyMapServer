package Response;

import DataModels.Person;

public class PersonIDResponse extends PersonResponseObject{
    public PersonIDResponse(Person person) {
        super(person);
        success = true;
    }

    public boolean success;
}
