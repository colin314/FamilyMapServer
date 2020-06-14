package DataAccess;

import Model.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a person into the Persons table
     * @param person the person to insert.
     * @throws DataAccessException if there is a problem inserting the person (such as the person_ID already existing in
     * the table).
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (Person_ID, Username, First_Name, Last_Name, Gender, Father_ID, Mother_ID, Spouse_ID)" +
                " VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.personID);
            stmt.setString(2, person.associatedUsername);
            stmt.setString(3, person.firstName);
            stmt.setString(4, person.lastName);
            stmt.setString(5, person.gender);
            stmt.setString(6, person.fatherID);
            stmt.setString(7, person.motherID);
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Finds the Person in the database with the given PersonID
     * @param personID PersonID of person being sought
     * @return Person as a Person object, null if the ID doesn't correspond to a person.
     * @throws DataAccessException if there is a problem executing the query.
     */
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
     * Retrieves all of the persons associated with a given user
     * @param userName user name
     * @return An array list of the persons
     * @throws DataAccessException if there is a problem executing the SQL query
     */
    public ArrayList<Person> findByUser(String userName) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                persons.add(new Person(rs.getString("Person_ID"), rs.getString("Username"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"), rs.getString("Gender"), rs.getString("Father_ID"),
                        rs.getString("Mother_ID"), rs.getString("Spouse_ID")));
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
        return persons;
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

    /**
     * Clears all persons associated with a specific user.
     * @param userName the user name
     * @throws DataAccessException if there is a problem encountered when deleting records.
     */
    public void clearByUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Persons Table: " + e.getMessage());
        }
    }
}