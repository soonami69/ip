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
        String[] items = new String[100];
        int count = 0;
        System.out.println("____________________________________________________________");
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().trim();
            System.out.println("____________________________________________________________");
            // user exiting
            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("Clonky: Get out!");
                System.out.println("____________________________________________________________");
                break;
            }
            if (userInput.equalsIgnoreCase("list")) {
                if (count == 0) {
                    System.out.println("No items!");
                } else {
                    for (int i = 0; i < count; i++) {
                        System.out.println(items[i]);
                    }
                }
            } else {
                System.out.println("Clonky: " + userInput);
                items[count] = userInput;
                count += 1;
            }
            System.out.println("____________________________________________________________");
        }
    }
}
