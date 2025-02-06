package clonky.exceptions;

/**
 * An exception when a Task is constructed with incorrect arguments.
 */
public class InvalidTaskFormatException extends Exception {
    public InvalidTaskFormatException(String message) {
        super(message);
    }
}
