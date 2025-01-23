class TaskManager {
    private final Task[] tasks = new Task[100];
    private int count = 0;

    public void addTodo(String description) throws NoDescriptionException {
        if (description == null || description.trim().isEmpty()) {
            throw new NoDescriptionException("Todo");
        }
        ToDos newTodo = new ToDos(description.trim());
        tasks[count++] = newTodo;
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
        tasks[count++] = newDeadline;
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
        tasks[count++] = newEvent;
        System.out.printf("New event \"%s\" has been successfully eaten.\n", newEvent.description);
        printTaskCount();
    }

    public void markTask(String argument) {
        int index = parseIndex(argument);
        if (index == -1) return;

        if (tasks[index].isDone) {
            System.out.println("Task already complete!");
        } else {
            tasks[index].markAsDone();
            System.out.println("Task " + tasks[index].description + " successfully marked.");
            System.out.println(tasks[index]);
        }
    }

    public void unmarkTask(String argument) {
        int index = parseIndex(argument);
        if (index == -1) return;

        if (!tasks[index].isDone) {
            System.out.println("Task already incomplete!");
        } else {
            tasks[index].markAsUndone();
            System.out.println("Task " + tasks[index].description + " successfully unmarked.");
            System.out.println(tasks[index]);
        }
    }

    public void listTasks() {
        if (count == 0) {
            System.out.println("Nothing in my stomach. Feed me!");
            return;
        }
        System.out.println("Here's what I've eaten so far:");
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println("FEED ME MORE!");
    }

    private int parseIndex(String argument) {
        try {
            int index = Integer.parseInt(argument.trim()) - 1;
            if (index < 0 || index >= count) {
                System.out.printf("Please provide a number between 1 and %d!\n", count);
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            System.out.println("Please provide a proper number!");
            return -1;
        }
    }

    private void printTaskCount() {
        System.out.printf("Now I have %d tasks in my tummy!\n", count);
    }
}
