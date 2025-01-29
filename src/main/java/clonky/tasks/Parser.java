package clonky.tasks;

import java.util.List;

/**
 * The {@code Parser} class handles the parsing and execution of user commands
 * related to task management in the Clonky application.
 * It supports adding, removing, marking, and saving/loading tasks.
 */
public class Parser {
    private final TaskList taskList = new TaskList();

    /**
     * Adds a new {@code Todo} task to the list.
     *
     * @param description The description of the to-do task.
     * @throws NoDescriptionException If the description is empty or null.
     */
    public void addTodo(String description) throws NoDescriptionException {
        taskList.addTodo(description);
    }

    /**
     * Adds a new {@code Deadline} task with a due date.
     *
     * @param arguments The task description and due date in the format "description /by YYYY-MM-DD".
     * @throws NoDescriptionException If the description is missing.
     * @throws NoByException If the due date is missing.
     */
    public void addDeadline(String arguments) throws NoDescriptionException, NoByException {
        taskList.addDeadline(arguments);
    }

    /**
     * Adds an event task by passing the arguments to the task list.
     *
     * @param arguments The arguments containing the description, start, and end dates.
     * @throws NoDescriptionException If the description is empty.
     * @throws NoFromException       If the '/from' part is missing.
     * @throws NoToException         If the '/to' part is missing.
     */
    public void addEvent(String arguments) throws NoDescriptionException, NoFromException, NoToException {
        taskList.addEvent(arguments);
    }

    /**
     * Marks a task as done based on the given index.
     *
     * @param arguments The index of the task to mark as done.
     */
    public void markTask(String arguments) {
        int index = parseIndex(arguments);
        if (index == -1) return;
        taskList.markTask(index);
    }

    /**
     * Unmarks a task based on the given index.
     *
     * @param arguments The index of the task to unmark.
     */
    public void unmarkTask(String arguments) {
        int index = parseIndex(arguments);
        if (index == -1) return;
        taskList.unmarkTask(index);
    }

    /**
     * Displays all tasks in the list.
     */
    public void listTasks() {
        taskList.listTasks();
    }

    private int parseIndex(String argument) {
        try {
            int index = Integer.parseInt(argument.trim()) - 1;
            if (index < 0 || index >= taskList.getSize()) {
                if (taskList.getSize() == 1) {
                    System.out.println("I'm only accepting the number 1 at the moment.");
                } else {
                    System.out.printf("Please provide a number between 1 and %d!\n",
                            taskList.getSize());
                }
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            System.out.println("Please provide a proper number!");
            return -1;
        }
    }

    /**
     * Removes a task based on the given index.
     *
     * @param arguments The index of the task to be removed.
     */
    public void removeTask(String arguments) {
        int index = parseIndex(arguments);
        if (index == -1) return;
        taskList.removeTask(index);
    }

    /**
     * Finds tasks that match the given description.
     *
     * @param description The description to search for.
     * @return A list of tasks that match the description.
     */
    public void find(String description) {
        String[] parts = description.split(" ", 2); // Split command and arguments
        String command = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";
        switch (command) {
        case ("desc"):
            List<Task> tasks = taskList.findTasksByDescription(arguments);
            System.out.printf("There are %d tasks that contained %s\n", tasks.size(),
                    arguments);
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d. %s\n", i + 1,
                        tasks.get(i).toString());
            }
            System.out.println("Find what you need?");
            break;
        case ("time"):
            System.out.println("Yea, I wish I had time too...");
            break;
        default:
            System.out.println("I don't quite understand that. Use " +
                    "find {query} to search for tasks");
        }
    }

    public boolean loadTasks() {
        return taskList.loadTasks();
    }

    public void saveTasks(String arguments) {
        taskList.saveTasks(arguments);
    }
}
