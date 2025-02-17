package clonky.exceptions;

/**
 * Exception that is thrown when an {@link clonky.tasks.Event} is not supplied with a Date for its "from"
 * field.
 */
public class NoPriorityException extends Exception {
    public NoPriorityException() {
        super("Specify a priority for your task before the description (e.g. todo 1 eat food)");
    }
}
