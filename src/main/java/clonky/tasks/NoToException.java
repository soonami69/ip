package clonky.tasks;

public class NoToException extends Exception {
    public NoToException() {
        super("Specify an end date for your event with \"/to {date}\"");
    }
}
