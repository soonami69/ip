package clonky.exceptions;

public class NoByException extends Exception {
    public NoByException() {
        super("Specify a deadline for your... well, deadline with \"/by {date}\"");
    }
}
