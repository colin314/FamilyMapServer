package Response;

import DataModels.Person;
import java.util.ArrayList;
import java.util.List;

public class PersonResponse {

    public ArrayList<Person> data;
    boolean success;

    public PersonResponse(List<Person> persons) {
        success = true;
        data = new ArrayList<Person>();
        for (Person person : persons) {
            data.add(new Person(person));
        }
    }
}
