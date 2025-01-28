import java.util.ArrayList;

class TaskManager {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public void addTodo(String description) throws NoDescriptionException {
        if (description == null || description.trim().isEmpty()) {
            throw new NoDescriptionException("Todo");
        }
        ToDos newTodo = new ToDos(description.trim());
        tasks.add(newTodo);
        System.out.printf("New todo \"%s\" has been successfully eaten.\n", newTodo.description);
        printTaskCount();
    }

    public void addDeadline(String arguments) throws NoDescriptionException, NoByException {
        String[] parts = arguments.split("/by", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new NoDescriptionException("Deadline");
        }
        if (parts[1].trim().isEmpty()) {
            throw new NoByException();
        }
        Deadline newDeadline = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(newDeadline);
        System.out.printf("New deadline \"%s\" has been successfully eaten.\n", newDeadline.description);
        printTaskCount();
    }

    public void addEvent(String arguments) throws NoFromException, NoToException, NoDescriptionException {
        String[] parts = arguments.split("/from", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new NoDescriptionException("Event");
        }
        String[] timeParts = parts[1].split("/to", 2);
        if (timeParts.length < 2 || timeParts[0].trim().isEmpty()) {
            throw new NoFromException();
        }
        if (timeParts[1].trim().isEmpty()) {
            throw new NoToException();
        }
        Event newEvent = new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim());
        tasks.add(newEvent);
        System.out.printf("New event \"%s\" has been successfully eaten.\n", newEvent.description);
        printTaskCount();
    }

    public void markTask(String argument) {
        int index = parseIndex(argument);
        if (index == -1) return;

        if (tasks.get(index).isDone) {
            System.out.println("Task already complete!");
        } else {
            tasks.get(index).markAsDone();
            System.out.println("Task " + tasks.get(index).description + " successfully marked.");
            System.out.println(tasks.get(index));
        }
    }

    public void unmarkTask(String argument) {
        int index = parseIndex(argument);
        if (index == -1) return;

        if (!tasks.get(index).isDone) {
            System.out.println("Task already incomplete!");
        } else {
            tasks.get(index).markAsUndone();
            System.out.println("Task " + tasks.get(index).description + " successfully unmarked.");
            System.out.println(tasks.get(index));
        }
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Nothing in my stomach. Feed me!");
            return;
        }
        System.out.println("Here's what I've eaten so far:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println("FEED ME MORE!");
    }

    private int parseIndex(String argument) {
        try {
            int index = Integer.parseInt(argument.trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                if (tasks.size() == 1) {
                    System.out.println("I'm only accepting the number 1 at the moment.");
                } else {
                    System.out.printf("Please provide a number between 1 and %d!\n", tasks.size());
                }
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            System.out.println("Please provide a proper number!");
            return -1;
        }
    }

    public void removeTask(String argument) {
        int index = parseIndex(argument);
        if (index == -1) return;
        tasks.remove(index);
        System.out.println("Task successfully ANNIHILATED :3");
    }

    private void printTaskCount() {
        System.out.printf("Now I have %d tasks in my tummy!\n", tasks.size());
    }
}
