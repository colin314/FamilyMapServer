package DataAccess;

import java.sql.*;
import java.util.UUID;

import DataAccess.*;
import Model.*;

public class ConnectionFactory {
    public static final String SERVER = "localhost/DIPPR";
    public static final String USER = "sa";
    public static final String PWD = "password";

    public static void main(String[] args) {
        Database db = new Database();
        try(Connection conn = db.openConnection()) {
            UserDAO userDAO = new UserDAO(conn);
            User user = userDAO.find("colin314");
            System.out.println(user.toString());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }

/**
        try(Connection conn = DriverManager.getConnection(connectionUrl);) {

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
 */
    }
}
