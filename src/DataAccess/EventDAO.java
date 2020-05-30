package DataAccess;

import Model.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDAO {
    private final Connection conn;

    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a event into the Events table
     * @param event the event to insert.
     * @throws DataAccessException if there is a problem inserting the event (such as the event_ID already existing in
     * the table).
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO Events (Event_ID, Username, Event_ID, Latitude, Longitude, " +
                "Country, City, EventType, Event_Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.eventID);
            stmt.setString(2, event.associatedUsername);
            stmt.setString(3, event.eventID);
            stmt.setDouble(4, event.latitude);
            stmt.setDouble(5, event.longitude);
            stmt.setString(6, event.country);
            stmt.setString(7, event.city);
            stmt.setString(8, event.eventType);
            stmt.setInt(9, event.year);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Retrieves all of the events associated with a given user
     * @param userName user name
     * @return An array list of the events
     * @throws DataAccessException if there is a problem executing the SQL query
     */
    public ArrayList<Event> findByUser(String userName) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                events.add(new Event(rs.getString("Event_ID"), rs.getString("Username"),
                        rs.getString("Event_ID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Event_Year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }

    /**
     * Finds the Event in the database with the given EventID
     * @param eventID EventID of event being sought
     * @return Event as a Event object, null if the ID doesn't correspond to a event.
     * @throws DataAccessException if there is a problem executing the query.
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE Event_ID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("Event_ID"), rs.getString("Username"),
                        rs.getString("Event_ID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Event_Year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
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
     * Clears the Events table of all non-user events
     * @throws DataAccessException if there is an error
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Events Table");
        }
    }

    /**
     * Clears all events associated with a specific user.
     * @param userName the user name
     * @throws DataAccessException if there is a problem encountered when deleting records.
     */
    public void clearByUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Events Table: " + e.getMessage());
        }
    }
}
