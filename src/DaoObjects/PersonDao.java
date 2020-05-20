package DaoObjects;

public class PersonDao {
    public PersonDao() {}

    public PersonDao(String person_ID, String username, String first_Name, String last_Name, String gender, String father_ID, String mother_ID, String spouse_ID) {
        Person_ID = person_ID;
        Username = username;
        First_Name = first_Name;
        Last_Name = last_Name;
        Gender = gender;
        Father_ID = father_ID;
        Mother_ID = mother_ID;
        Spouse_ID = spouse_ID;
    }

    public PersonDao(String username, String first_Name, String last_Name, String gender, String father_ID, String mother_ID, String spouse_ID) {
        Username = username;
        First_Name = first_Name;
        Last_Name = last_Name;
        Gender = gender;
        Father_ID = father_ID;
        Mother_ID = mother_ID;
        Spouse_ID = spouse_ID;
    }

    /**
     * Unique identifier for this person (non-empty string)
     */
    public String Person_ID;

    /**
     * Username to which this person belongs
     */
    public String Username;
    /**
     * Person's first name
     */
    public String First_Name;
    /**
     * Person's last name
     */
    public String Last_Name;
    /**
     * Person's gender (string: "f" or "m")
     */
    public String Gender;
    /**
     * Person ID of person's father (possibly null)
     */
    public String Father_ID;
    /**
     * Person ID of person's mother (possibly null)
     */
    public String Mother_ID;
    /**
     * Person ID of person's spouse (possibly null)
     */
    public String Spouse_ID;
}
