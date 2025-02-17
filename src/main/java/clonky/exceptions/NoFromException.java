package clonky.exceptions;

/**
 * Exception that is thrown when an {@link clonky.tasks.Event} is not supplied with a Date for its "from"
 * field.
 */
public class NoFromException extends Exception {
    public NoFromException() {
        super("Specify a starting date for your event with \"/from {date}\"");
    }
}
