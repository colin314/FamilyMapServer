package DataAccess;

import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insert(Person person) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Persons (Person_ID, Username, First_Name, Last_Name, Gender, Father_ID, Mother_ID, Spouse_ID)" +
                " VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.personID);
            stmt.setString(2, person.associatedUsername);
            stmt.setString(3, person.firstName);
            stmt.setString(4, person.lastName);
            stmt.setString(5, person.gender);
            stmt.setString(6, person.fatherID);
            stmt.setString(7, person.motherID);
            stmt.setString(8, person.spouseID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting person into the database");
        }
    }

    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE Person_ID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("Person_ID"), rs.getString("Username"),
                rs.getString("First_Name"), rs.getString("Last_Name"), rs.getString("Gender"), rs.getString("Father_ID"),
                        rs.getString("Mother_ID"), rs.getString("Spouse_ID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * Clears the Persons table of all non-user persons
     * @throws DataAccessException if there is an error
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Persons DB");
        }
    }
}