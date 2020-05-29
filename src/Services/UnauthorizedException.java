package Services;

public class UnauthorizedException extends Exception {
    UnauthorizedException(String message)
    {
        super(message);
    }

    UnauthorizedException()
    {
        super();
    }
}
