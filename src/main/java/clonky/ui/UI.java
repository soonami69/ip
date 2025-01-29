package clonky.ui;

import clonky.tasks.NoByException;
import clonky.tasks.NoDescriptionException;
import clonky.tasks.NoFromException;
import clonky.tasks.NoToException;
import clonky.tasks.Parser;
import clonky.tasks.UnknownCommandException;

import java.util.Scanner;

public class UI {
    private final Parser PARSER;
    private final Scanner scanner;

    public UI(Parser parser, Scanner scanner) {
        this.PARSER = parser;
        this.scanner = scanner;
    }

    public void startup() {
        String logo = "\uD83C\uDF4E ⋆ \uD83C\uDF52  \uD83C\uDF80  \uD835\uDC9E\uD835\uDCC1\uD83C\uDF51\uD835\uDCC3\uD835\uDCC0\uD835\uDCCE  \uD83C\uDF80  \uD83C\uDF52 ⋆ \uD83C\uDF4E";
        System.out.println("Hello! I'm \n" + logo + "\nFEED ME");
        System.out.println("____________________________________________________________");
        System.out.println("Loading tasks...");
        if (PARSER.loadTasks()) {
            System.out.println("Tasks successfully dug up!!!");
        } else {
            System.out.println("Tasks could not be loaded... I'll just ignore it for now...");
        }
        run();
    }

    private void run() {
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().trim();
            System.out.println("____________________________________________________________");

            if (userInput.equals("bye")) {
                System.out.println("Go away!");
                break;
            }
            try {
                handleCommand(userInput);
            } catch (UnknownCommandException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("____________________________________________________________");
        }
    }

    private void handleCommand(String userInput) throws UnknownCommandException {
        String[] parts = userInput.split(" ", 2); // Split command and arguments
        String command = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (command) {
        case "todo":
            try {
                PARSER.addTodo(arguments);
            } catch (NoDescriptionException e) {
                System.out.println(e.getMessage());
            }
            break;
        case "deadline":
            try {
                PARSER.addDeadline(arguments);
            } catch (NoDescriptionException | NoByException e) {
                System.out.println(e.getMessage());
            }
            break;
        case "event":
            try {
                PARSER.addEvent(arguments);
            } catch (NoDescriptionException | NoFromException | NoToException e) {
                System.out.println(e.getMessage());
            }
            break;
        case "hello":
            System.out.println("Hi!. What would you like to do today?");
            break;
        case "save":
            PARSER.saveTasks(arguments);
            break;
        case "load":
            PARSER.loadTasks();
            break;
        case "mark":
            PARSER.markTask(arguments);
            break;
        case "unmark":
            PARSER.unmarkTask(arguments);
            break;
        case "find":
            PARSER.find(arguments);
            break;
        case "ANNIHILATE":
            PARSER.removeTask(arguments);
            // This is a deliberate fall through so that it will lists tasks after one is removed.
        case "list":
            PARSER.listTasks();
            break;
        default:
            throw new UnknownCommandException(command);
        }
    }
}
