package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;

import java.sql.Connection;

public abstract class Service {
    Service () {
        db = new Database();
    }

    Service (Connection conn) {
        db = null;
    }

    protected Database db;

    protected void closeConnection(boolean commit) {
        if (db != null) {
            try {
                db.closeConnection(commit);
            }
            catch (DataAccessException ex) {

            }
        }
    }
}
