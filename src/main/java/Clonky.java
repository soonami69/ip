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


        System.out.println("Hello! I'm \n" + logo + "\nWhat can I do for you?");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().trim();

            // user exiting
            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("Clonky: Get out!");
                break;
            }

            System.out.println("____________________________________________________________");
            System.out.println("Clonky: " + userInput);
            System.out.println("____________________________________________________________");
        }
    }
}
