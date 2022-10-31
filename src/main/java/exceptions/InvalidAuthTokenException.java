package exceptions;

public class InvalidAuthTokenException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid Auth token";
    }
}
