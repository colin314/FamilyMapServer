package Model;

import Request.RegisterRequest;

import java.util.UUID;

public class User {
    public User() {}

    public User(RegisterRequest request) {
        userName = request.userName;
        password = request.password;
        email = request.email;
        firstName = request.firstName;
        lastName = request.lastName;
        gender = request.gender;
        personID = UUID.randomUUID().toString();
    }

    public User(String userName, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String gender;
    String personID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != User.class) {
            return false;
        }
        User that = (User) obj;
        return this.userName.equalsIgnoreCase(that.userName);
    }

    public boolean fullEquals(User that) {
        if (!this.equals(that)) {
            return false;
        }
        if (!this.password.equals(that.password)) { return false; }
        if (!this.email.equalsIgnoreCase(that.email)) { return false; }
        if ((this.firstName == null ^ that.firstName == null) ||
                this.lastName != null && !this.firstName.equalsIgnoreCase(that.firstName)) { return false; }
        if ((this.lastName == null ^ that.lastName == null) ||
                this.lastName != null && !this.lastName.equalsIgnoreCase(that.lastName)) { return false; }
        if ((this.gender == null ^ that.gender == null) ||
                this.gender != null && !this.gender.equalsIgnoreCase(that.gender)){ return false; }
        if (!this.personID.equalsIgnoreCase(that.personID)) { return false; }
        return true;
    }
}
