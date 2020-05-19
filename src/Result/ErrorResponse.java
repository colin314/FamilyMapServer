package Result;

/**
 * An error response that inherits from exception so it can be thrown.
 */
public class ErrorResponse extends Exception {
    public ErrorResponse(String message) {
        this.message = message;
        success = false;
    }

    /**
     * Description of the error.
     */
    public String message;
    /**
     * Flag stating that the task failed.
     */
    public boolean success;
}
