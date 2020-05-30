package Services;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Result.FamilyMapException;
import Result.Response;
import com.github.javafaker.Faker;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Responsible for executing fill requests.
 */
public class FillService extends Service{
    /**
     * Creates a connection to the database and opens it.
     * @throws FamilyMapException if there is a problem opening the connections (bad login, bad server address
     * etc.)
     */
    public FillService() throws FamilyMapException {
        super();
        try {
            userDAO = new UserDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            personDAO = new PersonDAO(db.getConnection());
            authTokenDAO = new AuthTokenDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
    }
    /**
     * Allows an existing connection to be used in this service.
     * @param conn An existing open database connection
     */
    public FillService(Connection conn) {
        super(conn);
        userDAO = new UserDAO(conn);
        eventDAO = new EventDAO(conn);
        personDAO = new PersonDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
    }

    UserDAO userDAO;
    PersonDAO personDAO;
    EventDAO eventDAO;
    AuthTokenDAO authTokenDAO;
    Faker faker = new Faker();

    /**
     * Overload of {@code fillDatabase(String username, int generations)} that puts in
     * default value of 4 for {@code generations} parameter.
     * @param username The username to execute the fill for.
     * @return A Response object detailing how many persons and events were added to
     * the database.
     * @exception FamilyMapException if the username is invalid.
     * @exception FamilyMapException if there was an Internal server error.
     */
    public Response fillDatabase(String username) throws FamilyMapException {
        return fillDatabase(username, 4);
    }

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server.
     * If there is any data in the database already associated with the given user name, it is
     * deleted. The optional "generations" parameter lets the caller specify the number of
     * generations of ancestors to be generated, and must be a non-negative integer (the
     * default is 4, which results in 31 new persons each with associated events).
     * @param username The username to execute the fill for.
     * @param generations The number of generations to generate.
     * @return A Response object detailing how many persons and events were added to
     * he database.
     * @exception FamilyMapException if the username or generations parameter is invalid.
     * @exception FamilyMapException if there was an Internal server error.
     */
    public Response fillDatabase(String username, int generations) throws FamilyMapException {
        try {
            User user = userDAO.find(username);
            if (user == null) {
                closeConnection(false);
                throw new FamilyMapException("Error executing fill, the given username does not correspond" +
                        String.format("to a known username. Given username: %s", username));
            }
            personDAO.clearByUser(username);
            eventDAO .clearByUser(username);

            //Generate User's Person
            Person rootPerson = new Person(user.getPersonID(), user.getUserName(), user.getFirstName(),
                    user.getLastName(), user.getGender(), null, null, null);
            Location birthLoc = getRandomLocation();
            Event rootBirth = new Event(UUID.randomUUID().toString(), user.getUserName(), rootPerson.getPersonID(),
                    birthLoc.latitude, birthLoc.longitude, birthLoc.country, birthLoc.city,
                    "Birth", faker.number().numberBetween(1900, 2012));

            //Populate family tree
            FamilyTreeNode rootNode = new FamilyTreeNode(rootPerson);
            rootNode.setEvents(new Event[3]);
            rootNode.getEvents()[0] = rootBirth;
            populateTree(rootNode, 0, generations);

            //Transfer family tree to database
            personDAO.insert(rootPerson);
            eventDAO.insert(rootBirth);
            fillDatabase(rootNode.getFather());
            fillDatabase(rootNode.getMother());
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        catch (FileNotFoundException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }

        int personCount = (int)Math.pow(2.0D, (generations + 1)) - 1;
        int evenCount = personCount * 3;
        String msg = String.format("Successfully added %d persons and %d events to the database.",personCount, evenCount);
        var rv = new Response(msg, true);
        closeConnection(true);
        return rv;
    }

    /**
     * Recursively creates a family tree from the given node.
     * @param childNode The current child node
     * @param depth The current "depth" of the tree (number of generations from root child)
     * @param maxDepth The maximum "depth" of the tree
     * @throws FamilyMapException if necessary files for generating random data are not found.
     */
    private void populateTree(FamilyTreeNode childNode, int depth, int maxDepth) throws FamilyMapException {
        try {
            if (!(depth < maxDepth)) {
                return;
            }
            generateParents(childNode);
            populateTree(childNode.getFather(), depth + 1, maxDepth);
            populateTree(childNode.getMother(), depth + 1, maxDepth);
        }
        catch (FileNotFoundException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
    }

    /**
     * Recursively fills the database with the information stored in the family tree.
     * @param childNode The current child node
     * @throws DataAccessException if there is an error inserting the record into the database (such
     * as a duplicate key)
     */
    private void fillDatabase(FamilyTreeNode childNode) throws DataAccessException {
        if (childNode == null) {
            return;
        }

        personDAO.insert(childNode.thisPerson);

        for (Event event : childNode.getEvents()) {
            eventDAO.insert(event);
        }

        fillDatabase(childNode.getFather());
        fillDatabase(childNode.getMother());
    }

    /**
     * Creates parents for the given childNode that meet required
     * constraints (born 13 years or more before the child is born, etc.)
     * @param childNode The node that corresponds to the current child.
     */
    private void generateParents(FamilyTreeNode childNode) throws FileNotFoundException {
        //Generate person objects (father & mother)
        Person child = childNode.getThisPerson();
        Person father = new Person(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                faker.name().firstName(), faker.name().lastName(), "m", null, null, null);
        Person mother = new Person(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                faker.name().firstName(), faker.name().lastName(), "f", null, null, father.getPersonID());
        father.setSpouseID(mother.getPersonID());

        //Populate birth and death events
        Event[] events = childNode.getEvents();
        int childBirth = events[0].year;
        Event[] fatherEvents = new Event[3];
        Event[] motherEvents = new Event[3];
        generateBirthDeathEvents(fatherEvents, childBirth, child.getAssociatedUsername(), father.personID);
        generateBirthDeathEvents(motherEvents, childBirth, child.getAssociatedUsername(), mother.personID);

        //Populate marriage event
        int minMarriage = Math.max(
                fatherEvents[0].year + 13, motherEvents[0].year + 13);
        int maxMarriage = childBirth;
        int marriageYear = faker.number().numberBetween(minMarriage, maxMarriage);
        Location marriageLoc = getRandomLocation();

        fatherEvents[1] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                father.getPersonID(), marriageLoc.latitude, marriageLoc.longitude,
                marriageLoc.country,marriageLoc.city, "Marriage", marriageYear);
        motherEvents[1] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                mother.getPersonID(), marriageLoc.latitude, marriageLoc.longitude,
                marriageLoc.country, marriageLoc.city, "Marriage", marriageYear);

        //Create nodes and attach to child
        FamilyTreeNode fatherNode = new FamilyTreeNode(father);
        fatherNode.setEvents(fatherEvents);
        FamilyTreeNode motherNode = new FamilyTreeNode(mother);
        motherNode.setEvents(motherEvents);

        child.setFatherID(father.getPersonID());
        child.setMotherID(mother.getPersonID());
        childNode.setFather(fatherNode);
        childNode.setMother(motherNode);
    }

    /**
     * Creates birth and death events for a parent based on the child's birth year
     * @param events Array of size 3 which contains, in this order, birth, marriage, and death events.
     * @param childBirth The year the child was born
     * @param userName The associated username for the event records
     * @param uuid The personID of the parent
     * @throws FileNotFoundException if the file of random locations cannot be found.
     */
    private void generateBirthDeathEvents(Event[] events, int childBirth, String userName,
                                          String uuid) throws FileNotFoundException {
        int maxBirth = childBirth - 13;
        int minBirth = childBirth - 50;

        int birthYear = faker.number().numberBetween(minBirth,maxBirth);
        int deathYear = faker.number().numberBetween(childBirth, birthYear + 120);
        Location birthLoc = getRandomLocation();
        Location deathLoc = getRandomLocation();

        events[0] = new Event(UUID.randomUUID().toString(), userName,
                uuid, birthLoc.latitude, birthLoc.longitude,
                birthLoc.country, birthLoc.city, "Birth", birthYear);
        events[2] = new Event(UUID.randomUUID().toString(), userName,
                uuid, deathLoc.latitude, deathLoc.longitude,
                deathLoc.country, deathLoc.city, "Death", deathYear);
    }

    /**
     * Retrieves a random location from the list of locations
     * @return A random location object
     * @throws FileNotFoundException if the file containing the list cannot be found.
     */
    private Location getRandomLocation() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader("Resources/familymapserver/json/locations.json"));
        LocationList locations = (new Gson().fromJson(jsonReader, LocationList.class));
        int locationIndex = faker.number().numberBetween(0, locations.data.size() - 1);
        return locations.data.get(locationIndex);
    }

    private class FamilyTreeNode {
        public FamilyTreeNode(Person person) {
            thisPerson = person;
        }
        Person thisPerson;
        FamilyTreeNode father;
        FamilyTreeNode mother;
        Event[] events;
        public FamilyTreeNode getFather() {
            return father;
        }

        public void setFather(FamilyTreeNode father) {
            this.father = father;
        }

        public FamilyTreeNode getMother() {
            return mother;
        }

        public void setMother(FamilyTreeNode mother) {
            this.mother = mother;
        }

        public Person getThisPerson() {
            return thisPerson;
        }

        public Event[] getEvents() {
            return events;
        }

        public void setEvents(Event[] events) {
            this.events = events;
        }
    }

    private class LocationList {
        public LocationList() {}
        ArrayList<Location> data;
    }

    private class Location {
        public Location() {}

        public String country;
        public String city;
        public float latitude;
        public float longitude;
    }
}
