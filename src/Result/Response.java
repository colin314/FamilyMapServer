package Result;

public class Response {
    public Response() {}
    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    public Response(FamilyMapException res) {
        message = res.getMessage();
        success = false;
    }

    public String message;
    public boolean success;
}
