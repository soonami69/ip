package clonky.handler;

import java.awt.Color;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import clonky.exceptions.NoByException;
import clonky.exceptions.NoDescriptionException;
import clonky.exceptions.NoFromException;
import clonky.exceptions.NoToException;
import clonky.exceptions.UnknownCommandException;
import clonky.response.Mood;
import clonky.response.Response;
import clonky.tasks.Parser;

/**
 * Handles user interaction for the Clonky application.
 * The handler class takes user input, processes commands, and interacts with the {@link Parser}.
 * The handler returns a {@link Response} when getResponse is called.
 */
public class Handler {
    private final Parser parser;
    private final Scanner scanner;

    /**
     * Constructs a new UI instance with a given parser and scanner.
     *
     * @param parser  The parser responsible for processing commands.
     * @param scanner The scanner to read user input.
     */
    public Handler(Parser parser, Scanner scanner) {
        this.parser = parser;
        this.scanner = scanner;
    }

    /**
     * Returns the Welcome message.
     * @return {@link Response} that contains the text and color for the welcome dialog box
     */
    public Response start() {
        return parser.welcome();
    }
    public Response loadTasks() {
        return parser.loadTasks();
    }

    public Response getResponse(String input) {
        try {
            return handleCommand(input);
        } catch (UnknownCommandException e) {
            return new Response(e.getMessage(), Mood.CHAOTIC, Color.WHITE);
        }
    }

    private Response handleCommand(String userInput) throws UnknownCommandException {
        String[] parts = userInput.split(" ", 2); // Split command and arguments
        String command = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (command) {
        case "todo":
            try {
                return parser.addTodo(arguments);
            } catch (NoDescriptionException e) {
                return new Response(e.getMessage(), Mood.SAD, Color.RED);
            }
        case "deadline":
            try {
                return parser.addDeadline(arguments);
            } catch (NoDescriptionException | NoByException e) {
                return new Response(e.getMessage(), Mood.SAD, Color.RED);
            } catch (DateTimeParseException e) {
                return new Response("Please provide a date in the format YYYY-MM-DD", Mood.SAD, Color.RED);
            }
        case "event":
            try {
                return parser.addEvent(arguments);
            } catch (NoDescriptionException | NoFromException | NoToException e) {
                return new Response(e.getMessage(), Mood.SAD, new Color(255, 116, 108));
            } catch (DateTimeParseException e) {
                return new Response("Please provide a date in the format YYYY-MM-DD", Mood.SAD, Color.RED);
            }
        case "hello":
            return new Response("Hi!. What would you like to do today?",
                    Mood.HAPPY, Color.yellow);
        case "save":
            return parser.saveTasks(arguments);
        case "load":
            return parser.loadTasks();
        case "mark":
            return parser.markTask(arguments);
        case "unmark":
            return parser.unmarkTask(arguments);
        case "find":
            return parser.find(arguments);
        case "ANNIHILATE":
            return parser.removeTask(arguments);
        case "list":
            return parser.listTasks();
        case "bye":
            javafx.application.Platform.exit();
            return new Response("Bye!", Mood.SAD, new Color(108, 116, 255));
        default:
            throw new UnknownCommandException(command);
        }
    }
}
