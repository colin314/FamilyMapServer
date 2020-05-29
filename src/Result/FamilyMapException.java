package Result;

public class FamilyMapException extends Throwable {
    public FamilyMapException() {}
    public FamilyMapException(String message) {
        this.message = message;
    }

    public String message;
}
