public class NoFromException extends Exception{
    public NoFromException() {
        super("Specify a starting date for your event with \"/from {date}\"");
    }
}
