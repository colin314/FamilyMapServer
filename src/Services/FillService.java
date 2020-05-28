package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.Response;
import java.lang.UnsupportedOperationException;
import java.sql.Connection;
import java.util.UUID;

/**
 * Responsible for executing fill requests.
 */
public class FillService extends Service{
    public FillService() throws Response {
        super();
        try {
            userDAO = new UserDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            personDAO = new PersonDAO(db.getConnection());
            authTokenDAO = new AuthTokenDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new Response(ex.getMessage(), false);
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

    /**
     * Overload of {@code fillDatabase(String username, int generations)} that puts in
     * default value of 4 for {@code generations} parameter.
     * @param username The username to excute the fill for.
     * @return A Response object detailing how many persons and events were added to
     * the database.
     * @exception Response if the username is invalid.
     * @exception Response if there was an Internal server error.
     */
    public Response fillDatabase(String username) throws Response {
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
     * @exception Response if the username or generations parameter is invalid.
     * @exception Response if there was an Internal server error.
     */
    public Response fillDatabase(String username, int generations) throws Response {
        //Make sure user is registered
        try {
            User user = userDAO.find(username);
            if (user == null) {
                closeConnection(false);
                throw new Response("The username does not correspond to a known user", false);
            }
            personDAO.clearByUser(username);
            eventDAO.clearByUser(username);
            //Recreate the user's Person object
            Person rootPerson = new Person(user.personID, user.userName, user.firstName, user.lastName,
                    user.gender, null, null, null);
            FamilyTreeNode rootNode = new FamilyTreeNode(rootPerson);
            populateTree(rootNode, 0, generations);
            fillDatabase(rootNode);
        }
        catch (DataAccessException ex) {
            closeConnection(false);
            throw new Response(ex.getMessage(), false);
        }
        int personCount = (int)Math.pow(2.0, generations);
        int evenCount = personCount * 3;
        String msg = String.format("Successfully added %d persons and %d events to the database.",personCount, evenCount);
        Response rv = new Response(msg, true);
        closeConnection(true);
        return rv;
    }

    private void populateTree(FamilyTreeNode childNode, int depth, int maxDepth) {
        if (!(depth < maxDepth)) {
            return;
        }
        Person child = childNode.thisPerson;
        Person father = new Person(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                null, null, "m", null, null, null);
        Person mother = new Person(UUID.randomUUID().toString(), child.getAssociatedUsername(),
                null, null,"f", null, null, father.personID);
        father.setSpouseID(mother.getPersonID());
        child.setFatherID(father.personID);
        child.setMotherID(mother.personID);
        FamilyTreeNode fatherNode = new FamilyTreeNode(father);
        FamilyTreeNode motherNode = new FamilyTreeNode(mother);
        childNode.setFather(fatherNode);
        populateTree(fatherNode, depth + 1, maxDepth);
        childNode.setMother(motherNode);
        populateTree(motherNode, depth + 1, maxDepth);
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

    private class FamilyTreeNode {
        public FamilyTreeNode(Person person) {
            thisPerson = person;
            events = new Event[3];
            events[0] = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.personID,
                    0,0,null,null,"Birth",0);
            events[1] = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.personID,
                    0,0,null,null,"Death",0);
            events[2] = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.personID,
                    0,0,null,null,"Marriage", 0);

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
    }
}
