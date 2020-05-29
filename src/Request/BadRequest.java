package Request;

public class BadRequest extends Throwable {
    public BadRequest(String message) {
        super(message);
    }

    public BadRequest() {super();}
}
