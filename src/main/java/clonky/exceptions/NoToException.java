package clonky.exceptions;

/**
 * Exception that is throne when an {@link clonky.tasks.Event}
 * is not supplied with a Date for its "to" field
 */
public class NoToException extends Exception {
    public NoToException() {
        super("Specify an end date for your event with \"/to {date}\"");
    }
}
