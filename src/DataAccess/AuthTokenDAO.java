package DataAccess;

import Model.AuthToken;
import Model.Person;
import Model.User;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private final Connection conn;

    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(AuthToken token) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO AuthTokens (Token, Username)" +
                "VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //            //Using the statements built-in set(type) functions we can pick the question mark we want
            //            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, token.token);
            stmt.setString(2, token.username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting user into the database");
        }
    }

    public String findUser(String authStr) throws DataAccessException {
        String user = null;
        ResultSet rs = null;
        String sql = "SELECT Username FROM AuthTokens WHERE Token == '?'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authStr);
            rs = stmt.executeQuery();
            if (rs.next()){
                user = rs.getString("Username");
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
        return user;
    }
}