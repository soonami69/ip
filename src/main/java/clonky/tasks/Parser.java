package clonky.tasks;

import java.awt.Color;
import java.util.List;

import clonky.exceptions.NoByException;
import clonky.exceptions.NoDescriptionException;
import clonky.exceptions.NoFromException;
import clonky.exceptions.NoToException;
import clonky.response.Mood;
import clonky.response.Response;

/**
 * The {@code Parser} class handles the parsing and execution of user commands
 * related to task management in the Clonky application.
 * It supports adding, removing, marking, and saving/loading tasks.
 */
public class Parser {
    private final TaskList taskList = new TaskList();
    private String text;

    /**
     * Adds a new {@code Todo} task to the list.
     *
     * @param description The description of the to-do task.
     * @throws NoDescriptionException If the description is empty or null.
     */
    public Response addTodo(String description) throws NoDescriptionException {
        assert description != null : "Description cannot be null!";
        return taskList.addTodo(description);
    }

    /**
     * Adds a new {@code Deadline} task with a due date.
     *
     * @param arguments The task description and due date in the format "description /by YYYY-MM-DD".
     * @throws NoDescriptionException If the description is missing.
     * @throws NoByException If the due date is missing.
     */
    public Response addDeadline(String arguments) throws NoDescriptionException, NoByException {
        assert arguments != null : "Arguments cannot be null!";
        assert !arguments.trim().isEmpty() : "Arguments cannot be empty!";
        return taskList.addDeadline(arguments);
    }

    /**
     * Adds an event task by passing the arguments to the task list.
     *
     * @param arguments The arguments containing the description, start, and end dates.
     * @throws NoDescriptionException If the description is empty.
     * @throws NoFromException       If the '/from' part is missing.
     * @throws NoToException         If the '/to' part is missing.
     */
    public Response addEvent(String arguments) throws NoDescriptionException, NoFromException, NoToException {
        assert arguments != null : "Arguments cannot be null!";
        assert !arguments.trim().isEmpty() : "Arguments cannot be empty!";
        return taskList.addEvent(arguments);
    }

    /**
     * Removes a task based on the given index.
     *
     * @param arguments The index of the task to be removed.
     */
    public Response removeTask(String arguments) {
        assert arguments != null : "Arguments cannot be null!";
        text = "";
        int index = parseIndex(arguments);
        if (index == -1) {
            return new Response(text, Mood.SAD, Color.PINK);
        }
        return taskList.removeTask(index);
    }

    /**
     * Marks a task as done based on the given index.
     *
     * @param arguments The index of the task to mark as done.
     */
    public Response markTask(String arguments) {
        assert arguments != null : "Arguments cannot be null!";
        text = "";
        int index = parseIndex(arguments);
        if (index == -1) {
            return new Response(text, Mood.SAD, Color.RED);
        }
        return taskList.markTask(index);
    }

    /**
     * Unmarks a task based on the given index.
     *
     * @param arguments The index of the task to unmark.
     */
    public Response unmarkTask(String arguments) {
        assert arguments != null : "Arguments cannot be null!";
        text = "";
        int index = parseIndex(arguments);
        if (index == -1) {
            return new Response(text, Mood.SAD, Color.RED);
        }
        return taskList.unmarkTask(index);
    }

    /**
     * Displays all tasks in the list.
     */
    public Response listTasks() {
        assert taskList != null : "TaskList should not be null!";
        return taskList.listTasks();
    }

    private int parseIndex(String argument) {
        assert argument != null : "Argument cannot be null!";
        try {
            int index = Integer.parseInt(argument.trim()) - 1;
            assert taskList != null : "TaskList should not be null!";
            if (index < 0 || index >= taskList.getSize()) {
                if (taskList.getSize() == 1) {
                    text = "I'm only accepting the number 1 at the moment.\n";
                } else {
                    text = String.format("Please provide a number between 1 and %d!\n", taskList.getSize());
                }
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            text = "Please provide a proper number!";
            return -1;
        }
    }

    /**
     * Finds tasks that match the given description.
     *
     * @param description The description to search for.
     * @return A Response whose list of tasks that match the description.
     */
    public Response find(String description) {
        assert description != null : "Description cannot be null!";

        StringBuilder text = new StringBuilder();
        String[] parts = description.split(" ", 2); // Split command and arguments
        String command = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (command) {
        case "desc":
            List<Task> tasks = taskList.findTasksByDescription(arguments);
            assert tasks != null : "Task list should not be null!";
            text.append(String.format("There are %d tasks that contained %s\n", tasks.size(), arguments));
            for (int i = 0; i < tasks.size(); i++) {
                text.append(String.format("%d. %s\n", i + 1, tasks.get(i).toString()));
            }
            text.append("Find what you need?\n");
            break;
        case "time":
            text.append("Yea, I wish I had time too...\n");
            break;
        default:
            text.append("I don't quite understand that. Use find desc {query} to search for tasks by description");
        }
        return new Response(text.toString(), Mood.HAPPY, new Color(245, 208, 51));
    }

    /**
     * Loads tasks from a pre-determined filepath.
     * @return A Response indicating if the load has been successful or not.
     */
    public Response loadTasks() {
        return taskList.loadTasks();
    }

    /**
     * Returns a standard Welcome message
     * @return A Response containing the welcome message
     */
    public Response welcome() {
        return new Response("Welcome! I'm Clonky... I think, and I'm here to help!", Mood.CHAOTIC,
                new Color(231, 210, 124));
    }

    /**
     * Saves tasks to a specified filepath.
     * @param arguments The file path where tasks should be saved.
     * @return A Response indicating success or failure.
     */
    public Response saveTasks(String arguments) {
        assert arguments != null : "Arguments cannot be null!";
        assert !arguments.trim().isEmpty() : "File path cannot be empty!";
        return taskList.saveTasks(arguments);
    }
}
