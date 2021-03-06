package DataAccess;

import Model.Person;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts the given user into the Users Table
     * @param user the user to insert
     * @throws DataAccessException if the username is a duplicate
     */
    public void insert(User user) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Users (Username, Person_ID, User_Password, Email)" +
                "VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //            //Using the statements built-in set(type) functions we can pick the question mark we want
            //            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPersonID());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting user into the database: " + e.getMessage());
        }
    }

    /**
     * Finds a user using their username
     * @param userName the user name to look for
     * @return The User in a user object, null if the username is not found.
     * @throws DataAccessException if there is a problem executing the query.
     */
    public User find(String userName) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT u.Username, u.User_Password, u.Email, " +
                "p.First_Name, p.Last_Name, p.Gender, u.Person_ID " +
                "FROM Users u LEFT JOIN Persons p ON u.Person_ID = p.Person_ID " +
                "WHERE u.Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("User_Password"),
                        rs.getString("Email"), rs.getString("First_Name"), rs.getString("Last_Name"),
                        rs.getString("Gender"), rs.getString("Person_ID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user: " + e.getMessage());
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Removes all users from the Users table
     * @throws DataAccessException if there is a problem removing the users.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the Users table: " + e.getMessage());
        }
    }
}