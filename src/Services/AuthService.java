package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import Result.Response;

import java.sql.Connection;

public abstract class AuthService extends Service {
    AuthService() throws Response{
        super();
        try {
            tokenDAO = new AuthTokenDAO(db.getConnection());
        }
        catch (DataAccessException ex) {
            throw new Response(ex.getMessage(), false);
        }
    }

    AuthService(Connection conn) throws Response {
        super(conn);
        tokenDAO = new AuthTokenDAO(conn);
    }

    protected AuthTokenDAO tokenDAO;

    protected String verifyToken(String tokenStr) throws Response{
        String userName = null;
        try {
            userName = tokenDAO.findUser(tokenStr);
        }
        catch (DataAccessException ex) {
            throw new Response(ex.getMessage(), false);
        }
        return userName;
    }
}
