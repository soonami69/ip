package clonky.exceptions;

/**
 * Exception that is thrown when the parser cannot process the command.
 */
public class UnknownCommandException extends Exception {
    /**
     * Initializes the Exception with the unknown command.
     * @param errorMessage The command that was not recognised.
     */
    public UnknownCommandException(String errorMessage) {
        super(String.format("*embraces you gently* Hey, you know that {%s} is not a recognized command, right?",
                errorMessage));
    }
}
