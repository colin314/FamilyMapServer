package DataAccess;

import Model.AuthToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private final Connection conn;

    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts the given token into the AuthTokens table
     * @param token the token to insert
     * @throws DataAccessException if there is a problem inserting the token (such as
     * it being a duplicate token).
     */
    public void insert(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO AuthTokens (Token, Username)" +
                "VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token.token);
            stmt.setString(2, token.username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Returns the user associated with the given token (if they exist)
     * @param authStr the token
     * @return The user name of the user associated with the token, null otherwise.
     * @throws DataAccessException if there is a problem executing the query.
     */
    public String findUser(String authStr) throws DataAccessException {
        String user = null;
        ResultSet rs = null;
        String sql = "SELECT Username FROM AuthTokens WHERE Token = ?";
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
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    /**
     * Clears all tokens from the AuthTokens table
     * @throws DataAccessException if there is a problem clearing the tokens.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthTokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the AuthTokens Table");
        }
    }
}