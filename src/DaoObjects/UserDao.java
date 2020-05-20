package DaoObjects;

public class UserDao {
    public UserDao() {
    }

    public UserDao(String username, String person_ID, String user_Password, String email) {
        Username = username;
        Person_ID = person_ID;
        User_Password = user_Password;
        Email = email;
    }

    public UserDao(String person_ID, String user_Password, String email) {
        Person_ID = person_ID;
        User_Password = user_Password;
        Email = email;
    }

    /**
     * Unique user name
     */
    public String Username;
    /**
     * Unique Person ID assigned to this user's generated Person object
     */
    public String Person_ID;
    /**
     * User's password
     */
    public String User_Password;
    /**
     * User's email address
     */
    public String Email;
}
