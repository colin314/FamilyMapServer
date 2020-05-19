package Response;

import DataModels.Person;

public class PersonResponseObject {
    public PersonResponseObject(Person person) {
        associatedUsername = person.username;
        personID = person.personID;
        firstName = person.firstName;
        lastName = person.lastName;
        gender = person.gender;
        fatherID = person.fatherID;
        motherID = person.motherID;
        spouseID = person.spouseID;
    }
    /**
     * Name of user account this person belongs to
     */
    public String associatedUsername;
    /**
     * Person’s unique ID
     */
    public String         personID;
    /**
     * Person's first name
     */
    public String         firstName;
    /**
     * Person's last name
     */
    public String         lastName;
    /**
     * Person's gender
     */
    public String         gender;
    /**
     * ID of person's father (OPTIONAL)
     */
    public String         fatherID;
    /**
     * ID of person's mother (OPTIONAL)
     */
    public String         motherID;
    /**
     * ID of person's spouse (OPTIONAL)
     */
    public String         spouseID;
}
