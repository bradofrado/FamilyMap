package exceptions;

public class InvalidNumberOfGenerations extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of generations";
    }
}
