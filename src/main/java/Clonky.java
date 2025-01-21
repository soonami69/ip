import java.util.Scanner;

public class Clonky {
    public static void main(String[] args) {
        String logo = """
        _________ .__                 __           
        \\_   ___ \\|  |   ____   ____ |  | _____.__.
        /    \\  \\/|  |  /  _ \\ /    \\|  |/ <   |  |
        \\     \\___|  |_(  <_> )   |  \\    < \\___  |
         \\______  /____/\\____/|___|  /__|_ \\/ ____|
                \\/                 \\/     \\/\\/     
        """;


        System.out.println("Hello! I'm \n" + logo + "\nWhat would you like me to store?");
        Scanner scanner = new Scanner(System.in);
        Task[] items = new Task[100];
        int count = 0;
        System.out.println("____________________________________________________________");
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.next();
            System.out.println("____________________________________________________________");
            // user exiting
            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("Clonky: Get out!");
                break;
            }
            if (userInput.equalsIgnoreCase("mark")) {
                // scan in next integer and mark as completed
                int idx = scanner.nextInt() - 1;
                if (idx < 0 || idx > 100 || idx >= count) {
                    System.out.println("Invalid index!");
                    continue;
                }
                if (items[idx].isDone) {
                    System.out.println("Task already complete!");
                } else {
                    items[idx].markAsDone();
                    System.out.println("Task " + items[idx].description +
                            " successfully marked.");
                }
            } else if (userInput.equalsIgnoreCase("unmark")) {
                // scan in next integer and mark as uncompleted
                int idx = scanner.nextInt() - 1;
                if (idx < 0 || idx > 100 || idx >= count) {
                    System.out.println("Invalid index!");
                    continue;
                }
                if (!items[idx].isDone) {
                    System.out.println("Task already incomplete!");
                } else {
                    items[idx].markAsUndone();
                    System.out.println("Task " + items[idx].description +
                            " successfully unmarked.");
                }
            } else if (userInput.equalsIgnoreCase("list")) {
                System.out.println("Here is your list of tasks:");
                if (count == 0) {
                    System.out.println("No items!");
                } else {
                    for (int i = 0; i < count; i++) {
                        System.out.println((i + 1) + ". " + items[i].toString());
                    }
                }
            } else {
                System.out.println("Clonky: " + userInput);
                items[count] = new Task(userInput);
                count += 1;
            }
            System.out.println("____________________________________________________________");
        }
    }
}
