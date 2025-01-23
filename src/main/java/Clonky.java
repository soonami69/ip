import java.util.Scanner;

public class Clonky {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        String logo = "\uD83C\uDF4E ⋆ \uD83C\uDF52  \uD83C\uDF80  \uD835\uDC9E\uD835\uDCC1\uD83C\uDF51\uD835\uDCC3\uD835\uDCC0\uD835\uDCCE  \uD83C\uDF80  \uD83C\uDF52 ⋆ \uD83C\uDF4E";
        System.out.println("Hello! I'm \n" + logo + "\nFEED ME");
        System.out.println("____________________________________________________________");

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

    private static void handleCommand(String userInput) throws UnknownCommandException {
        String[] parts = userInput.split(" ", 2); // Split command and arguments
        String command = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "todo":
                try {
                    taskManager.addTodo(arguments);
                } catch (NoDescriptionException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "deadline":
                try {
                    taskManager.addDeadline(arguments);
                } catch (NoDescriptionException | NoByException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "event":
                try {
                    taskManager.addEvent(arguments);
                } catch (NoDescriptionException | NoFromException | NoToException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "hello":
                System.out.println("Hi bby <3. What would you like to do today?");
                break;
            case "mark":
                taskManager.markTask(arguments);
                break;
            case "unmark":
                taskManager.unmarkTask(arguments);
                break;
            case "ANNIHILATE":
                taskManager.removeTask(arguments);
                // This is a deliberate fall through so that it will lists tasks after one is removed.
            case "list":
                taskManager.listTasks();
                break;
            default:
                throw new UnknownCommandException(command);
        }
    }
}
