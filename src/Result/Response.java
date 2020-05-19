package Result;

public class Response extends Throwable {
    public Response() {}
    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String message;
    public boolean success;
}
