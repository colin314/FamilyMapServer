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

    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String userName;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public String gender;
    public String personID;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Username: ");
        builder.append(userName);
        builder.append("\nPassword: ");
        builder.append(password);
        builder.append("\nEmail: ");
        builder.append(email);
        builder.append("\nFirst name: ");
        builder.append(firstName);
        builder.append("\nLast name: ");
        builder.append(lastName);
        builder.append("\nGender: ");
        builder.append(gender);
        builder.append("\nPerson ID: ");
        builder.append(personID);
        builder.append("\n");
        return builder.toString();
    }
}
