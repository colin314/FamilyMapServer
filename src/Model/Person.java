package Model;

public class Person {
    public Person() {}

    public Person(Person person) {
        this.associatedUsername = person.associatedUsername;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.gender = person.gender;
        this.fatherID = person.fatherID;
        this.motherID = person.motherID;
        this.spouseID = person.spouseID;
        this.personID = person.personID;
    }

    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender,
                  String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    /**
     * ID of the person
     */
    public String personID;
    /**
     * Username to which this person belongs
     */
    public String associatedUsername;
    /**
     * Person's first name
     */
    public String firstName;
    /**
     * Person's last name
     */
    public String lastName;
    /**
     * Person's gender (string: "f" or "m")
     */
    public String gender;
    /**
     * Person ID of person's father (possibly null)
     */
    public String fatherID;
    /**
     * Person ID of person's mother (possibly null)
     */
    public String motherID;
    /**
     * Person ID of person's spouse (possibly null)
     */
    public String spouseID;


    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Person.class) {
            return false;
        }
        Person that = (Person) obj;
        return this.personID.equals(that.personID);
    }
}
