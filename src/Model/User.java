package Model;

import DaoObjects.*;

public class User {
    public User() {}

    public User(UserDao user, PersonDao person) {
        userName = user.Username;
        password = user.User_Password;
        email = user.Email;
        firstName = person.First_Name;
        lastName = person.Last_Name;
        gender = person.Gender.toLowerCase();
        personID = person.Person_ID;
    }

    public String userName;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public String gender;
    public String personID;
}
