import java.util.Scanner;

public class Clonky {
    public static void main(String[] args) {
        String logo = "\uD83C\uDF4E ⋆ \uD83C\uDF52  \uD83C\uDF80  \uD835\uDC9E\uD835\uDCC1\uD83C\uDF51\uD835\uDCC3\uD835\uDCC0\uD835\uDCCE  \uD83C\uDF80  \uD83C\uDF52 ⋆ \uD83C\uDF4E";


        System.out.println("Hello! I'm \n" + logo + "\nFEED ME");
        Scanner scanner = new Scanner(System.in);
        Task[] items = new Task[100];
        int count = 0;
        String desc = "", by = "", from = "", to = "", input = "";
        System.out.println("____________________________________________________________");
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.next();
            System.out.println("____________________________________________________________");
            // user exiting
            int idx;
            switch (userInput) {
                case ("todo"):
                    desc = scanner.nextLine().trim();
                    ToDos newTodo = new ToDos(desc);
                    items[count] = newTodo;
                    count++;
                    System.out.printf("New todo \"%s\" has been successfully eaten.\n",
                            newTodo.description);
                    System.out.printf("Now I have %d tasks in my tummy!\n", count);
                    break;
                case ("deadline"):
                    input = scanner.nextLine().trim();
                    Scanner scanTillBy = new Scanner(input);
                    scanTillBy.useDelimiter("/by");
                    if (scanTillBy.hasNext()) {
                        desc = scanTillBy.next().trim();
                    }
                    if (scanTillBy.hasNext()) {
                        by = scanTillBy.next().trim();
                    }
                    Deadline newDeadline = new Deadline(desc, by);
                    items[count] = newDeadline;
                    count++;
                    System.out.printf("New deadline \"%s\" has been successfully eaten.\n",
                            newDeadline.description);
                    System.out.printf("Now I have %d tasks in my tummy!\n", count);
                    break;
                case ("event"):
                    input = scanner.nextLine().trim();
                    Scanner scanFrom = new Scanner(input);
                    scanFrom.useDelimiter("/from");
                    if (scanFrom.hasNext()) {
                        desc = scanFrom.next().trim();
                    }
                    input = scanFrom.next().trim();
                    scanFrom = new Scanner(input);
                    scanFrom.useDelimiter("/to");
                    if (scanFrom.hasNext()) {
                        from = scanFrom.next().trim();
                    }
                    if (scanFrom.hasNext()) {
                        to = scanFrom.next().trim();
                    }
                    Event newEvent = new Event(desc, from, to);
                    items[count] = newEvent;
                    System.out.printf("New event \"%s\" has been successfully eaten.\n",
                            newEvent.description);
                    System.out.printf("Now I have %d tasks in my tummy!\n", count);
                    count++;
                    break;
                case ("mark"):
                    // scan in next integer and mark as completed
                    idx = scanner.nextInt() - 1;
                    if (idx < 0 || idx > 100 || idx >= count) {
                        System.out.println("Invalid index!");
                        break;
                    }
                    if (items[idx].isDone) {
                        System.out.println("Task already complete!");
                    } else {
                        items[idx].markAsDone();
                        System.out.println("Task " + items[idx].description +
                                " successfully marked.");
                    }
                    break;
                case ("unmark"):
                    // scan in next integer and mark as uncompleted
                    idx = scanner.nextInt() - 1;
                    if (idx < 0 || idx > 100 || idx >= count) {
                        System.out.println("Invalid index!");
                        break;
                    }
                    if (!items[idx].isDone) {
                        System.out.println("Task already incomplete!");
                    } else {
                        items[idx].markAsUndone();
                        System.out.println("Task " + items[idx].description +
                                " successfully unmarked.");
                    }
                    break;
                case ("list"):
                    if (count == 0) {
                        System.out.println("Nothing in my stomach. Feed me!");
                        break;
                    }
                    System.out.println("Here's what I've eaten so far:");
                    for (int i = 0; i < count; i++) {
                        System.out.println((i + 1) +  "." + items[i].toString());
                    }
                    System.out.println("FEED ME MORE!");
                    break;
                case ("bye"):
                    break;
                default:
                    System.out.println("Command not recognized, nerd.");
            }
            if (userInput.equals("bye")) {
                System.out.println("Clonky: Get out!");
                break;
            }

            System.out.println("____________________________________________________________");
        }
    }
}
