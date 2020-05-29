package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import Result.FamilyMapException;

import java.sql.Connection;

public abstract class AuthService extends Service {
    AuthService() throws FamilyMapException {
        super();
        try {
            tokenDAO = new AuthTokenDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
    }

    AuthService(Connection conn) throws FamilyMapException {
        super(conn);
        tokenDAO = new AuthTokenDAO(conn);
    }

    protected AuthTokenDAO tokenDAO;

    protected String verifyToken(String tokenStr) throws FamilyMapException {
        String userName = null;
        try {
            userName = tokenDAO.findUser(tokenStr);
        }
        catch (DataAccessException ex) {
            throw new FamilyMapException(ex.getMessage());
        }
        return userName;
    }
}
