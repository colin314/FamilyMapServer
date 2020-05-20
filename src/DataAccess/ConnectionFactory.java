package DataAccess;

import java.sql.*;

public class ConnectionFactory {
    public static final String SERVER = "localhost/DIPPR";
    public static final String USER = "sa";
    public static final String PWD = "password";

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv6Addresses", "True");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String connectionUrl =
                "jdbc:sqlserver://localhost\\DIPPR;" +
                "databaseName=FamilyMapDb;integratedSecurity=false;" +
                "encrypt=false;trustServerCertificate=true;" +
                "authentication=SqlPassword;user=sa;password=incorrect";

        try(Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost\\DIPPR","sa","incorrect");) {

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try(Connection conn = DriverManager.getConnection(connectionUrl);) {

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
