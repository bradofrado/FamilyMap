package results;

public class Result {
    /**
     * The result message either successful or error message
     */
    private String message;
    /**
     * Whether or not the request was successful or an error
     */
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success=success;
    }

    public void Result() {
        success = true;
    }
}
