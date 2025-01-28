package tasks;

public class UnknownCommandException extends Exception {
    public UnknownCommandException(String errorMessage) {
        super(String.format("*embraces you gently* Hey, you know that {%s} is not a recognized command, right?",
                errorMessage));
    }
}
