package clonky.exceptions;

/**
 * Exception that is thrown when there is no deadline specified in the constructor
 * for {@link clonky.tasks.Deadline}
 */
public class NoByException extends Exception {
    public NoByException() {
        super("Specify a deadline for your... well, deadline with \"/by {date}\"");
    }
}
