package Response;

import DataModels.Person;
import java.util.ArrayList;
import java.util.List;

public class PersonResponse {

    public ArrayList<PersonResponseObject> data;
    boolean success;

    public PersonResponse(List<Person> persons) {
        success = true;
        data = new ArrayList<PersonResponseObject>();
        for (Person person : persons) {
            data.add(new PersonResponseObject(person));
        }
    }
}
