package DaoObjects;

public class PersonDao {
    public PersonDao() {}

    /**
     * Unique identifier for this person (non-empty string)
     */
    public String Person_ID;

    /**
     * Username to which this person belongs
     */
    public String Username;
    /**
     * Person’s first name
     */
    public String First_Name;
    /**
     * Person’s last name
     */
    public String Last_Name;
    /**
     * Person’s gender (string: "f" or "m")
     */
    public String Gender;
    /**
     * Person ID of person’s father (possibly null)
     */
    public String Father_ID;
    /**
     * Person ID of person’s mother (possibly null)
     */
    public String Mother_ID;
    /**
     * Person ID of person’s spouse (possibly null)
     */
    public String Spouse_ID;
}
