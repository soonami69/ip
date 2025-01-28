package tasks;

public class NoDescriptionException extends Exception {
    public NoDescriptionException(String errorMessage) {
        super(String.format("Hey, you didn't give a description for this %s", errorMessage));
    }
}
