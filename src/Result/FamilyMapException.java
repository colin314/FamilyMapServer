package Result;

public class FamilyMapException extends Throwable {
    public FamilyMapException() {}
    public FamilyMapException(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String message;
    public boolean success;
}
