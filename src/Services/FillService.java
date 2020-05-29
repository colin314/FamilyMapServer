package Services;


import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Result.FamilyMapException;
import com.github.javafaker.Faker;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

/**
 * Responsible for executing fill requests.
 */
public class FillService extends Service{
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
    int personCount = 0;
    int eventCount = 0;
    Faker faker = new Faker();

    /**
     * Overload of {@code fillDatabase(String username, int generations)} that puts in
     * default value of 4 for {@code generations} parameter.
     * @param username The username to excute the fill for.
     * @return A Response object detailing how many persons and events were added to
     * the database.
     * @exception FamilyMapException if the username is invalid.
     * @exception FamilyMapException if there was an Internal server error.
     */
    public FamilyMapException fillDatabase(String username) throws FamilyMapException {
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
    public FamilyMapException fillDatabase(String username, int generations) throws FamilyMapException {
        //Make sure user is registered
        try {
            User user = userDAO.find(username);
            if (user == null) {
                closeConnection(false);
                throw new FamilyMapException("The username does not correspond to a known user");
            }
            personDAO.clearByUser(username);
            eventDAO.clearByUser(username);
            //Recreate the user's Person object
            Person rootPerson = new Person(user.personID, user.userName, user.firstName, user.lastName,
                    user.gender, null, null, null);
            Event rootBirth = new Event(UUID.randomUUID().toString(), user.userName, rootPerson.getPersonID(),
                    getRandomFloat(-90, 90), getRandomFloat(-180, 180), faker.address().country(),
                    faker.address().city(), "Birth", faker.number().numberBetween(1900, 2012));

            FamilyTreeNode rootNode = new FamilyTreeNode(rootPerson);
            rootNode.setEvents(new Event[3]);
            rootNode.getEvents()[0] = rootBirth;

            personDAO.insert(rootPerson);
            eventDAO.insert(rootBirth);

            populateTree(rootNode, 0, generations);
            fillDatabase(rootNode.getFather());
            fillDatabase(rootNode.getMother());
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new FamilyMapException(ex.getMessage());
        }
        int personCount = (int)Math.pow(2.0, generations) + 1;
        int evenCount = personCount * 3;
        String msg = String.format("Successfully added %d persons and %d events to the database.",personCount, evenCount);
        FamilyMapException rv = new FamilyMapException(msg);
        closeConnection(true);
        return rv;
    }

    private void populateTree(FamilyTreeNode childNode, int depth, int maxDepth) throws FamilyMapException {
        if (!(depth < maxDepth)) {
            return;
        }
        generateParents(childNode);
        populateTree(childNode.getFather(), depth + 1, maxDepth);
        populateTree(childNode.getMother(), depth + 1, maxDepth);
    }

    private void fillDatabase(FamilyTreeNode childNode) throws DataAccessException {
        if (childNode == null) {
            return;
        }
    //Insert Person
        personDAO.insert(childNode.thisPerson);
    //Insert Events
        for (Event event : childNode.getEvents()) {
            eventDAO.insert(event);
        }
    //Insert Parents
        fillDatabase(childNode.getFather());
        fillDatabase(childNode.getMother());
    }

    private void generateParents(FamilyTreeNode childNode){

            Person child = childNode.getThisPerson();
            Event[] events = childNode.getEvents();
            int childBirth = events[0].year;
            int maxBirth = childBirth - 13;
            int minBirth = childBirth - 50;
            
            int fatherBirth = faker.number().numberBetween(minBirth,maxBirth);
            int motherBirth = faker.number().numberBetween(minBirth,maxBirth);
            int fatherDeath = faker.number().numberBetween(childBirth, fatherBirth + 120);
            int motherDeath = faker.number().numberBetween(childBirth, motherBirth + 120);
            
            int minMarriage = Math.max(fatherBirth + 12, motherBirth + 12);
            int maxMarriage = childBirth - 1;
            int marriageYear = faker.number().numberBetween(minMarriage, maxMarriage);
            String marriageCountry = faker.address().country();
            String marriageCity = faker.address().city();
            double marriageLat = getRandomFloat(-90, 90);
            double marriageLong = getRandomFloat(-180, 180);

            Person father = new Person(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    faker.name().firstName(), faker.name().lastName(), "m", null, null, null);
            Person mother = new Person(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    faker.name().firstName(), faker.name().lastName(), "f", null, null, father.getPersonID());
            father.setSpouseID(mother.getPersonID());
            Event[] fatherEvents = new Event[3];

            fatherEvents[0] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    father.getPersonID(), getRandomFloat(-90, 90), getRandomFloat(-180, 180),
                    faker.address().country(), faker.address().city(), "Birth", fatherBirth);
            fatherEvents[1] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    father.getPersonID(), marriageLat, marriageLong, marriageCountry,
                    marriageCity, "Marriage", marriageYear);
            fatherEvents[2] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    father.getPersonID(), getRandomFloat(-90, 90), getRandomFloat(-180, 180),
                    faker.address().country(), faker.address().city(), "Death", fatherDeath);

            Event[] motherEvents = new Event[3];
            motherEvents[0] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    mother.getPersonID(), getRandomFloat(-90, 90), getRandomFloat(-180, 180),
                    faker.address().country(), faker.address().city(), "Birth", motherBirth);
            motherEvents[1] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    mother.getPersonID(), marriageLat, marriageLong, marriageCountry,
                    marriageCity, "Marriage", marriageYear);
            motherEvents[2] = new Event(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                    mother.getPersonID(), getRandomFloat(-90, 90), getRandomFloat(-180, 180),
                    faker.address().country(), faker.address().city(), "Death", motherDeath);

            FamilyTreeNode fatherNode = new FamilyTreeNode(father);
            fatherNode.setEvents(fatherEvents);
            FamilyTreeNode motherNode = new FamilyTreeNode(mother);
            motherNode.setEvents(motherEvents);

            child.setFatherID(father.getPersonID());
            child.setMotherID(mother.getPersonID());
            childNode.setFather(fatherNode);
            childNode.setMother(motherNode);

    }

    private double getRandomFloat(double min, double max) {
        double randNum = min + new Random().nextFloat() * (max - min);
        return randNum;
    }

    private long yearToms(int year) {
        return year * 365 * 24 * 60 * 60 * 1000L;
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
}
