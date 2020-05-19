package DaoObjects;

/**
 * Data access object for records from the 'Events' table. This is converted into an 'Event'
 * Data object for processing in the code.
 */
public class EventDao {
    public EventDao() {}

    /**
     * The unique ID of the event
     */
    public String Event_ID;
    /**
     * The username of the user who created this event
     */
    public String Username;
    /**
     * The unique ID of the person that this event belongs to
     */
    public String Person_ID;
    /**
     * Latitude of event’s location
     */
    public double Latitude;
    /**
     * Longitude of event’s location
     */
    public double Longitude;
    /**
     * Country in which event occurred
     */
    public String Country;
    /**
     * City in which event occurred
     */
    public String City;
    /**
     * Type of event (birth, baptism, christening, marriage, death, etc.)
     */
    public String EventType;
    /**
     * Year in which event occurred
     */
    public int Event_Year;
}
