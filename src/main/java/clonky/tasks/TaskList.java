package clonky.tasks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code TaskList} class manages a collection of tasks and provides methods
 * for adding, marking, unmarking, removing, and searching tasks.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Adds a todo task to the task list.
     *
     * @param description The description of the todo task.
     * @throws NoDescriptionException If the description is empty.
     */
    public void addTodo(String description) throws NoDescriptionException {
        if (description == null || description.trim().isEmpty()) {
            throw new NoDescriptionException("Todo");
        }
        Todo newTodo = new Todo(description.trim());
        tasks.add(newTodo);
        System.out.printf("New todo \"%s\" has been successfully eaten.\n", newTodo.description);
    }

    /**
     * Adds a deadline task to the task list.
     *
     * @param arguments The arguments containing the description and deadline.
     * @throws NoDescriptionException If the description is empty.
     * @throws NoByException         If the '/by' part is missing.
     */
    public void addDeadline(String arguments) throws NoDescriptionException, NoByException {
        String[] parts = arguments.split("/by", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            if (parts[0].trim().isEmpty()) {
                throw new NoDescriptionException("Deadline");
            }
            throw new NoByException();
        }
        Deadline newDeadline = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(newDeadline);
        System.out.printf("New deadline \"%s\" has been successfully eaten.\n", newDeadline.description);
    }

    /**
     * Adds an event task to the task list.
     *
     * @param arguments The arguments containing the description, start, and end dates.
     * @throws NoDescriptionException If the description is empty.
     * @throws NoFromException       If the '/from' part is missing.
     * @throws NoToException         If the '/to' part is missing.
     */
    public void addEvent(String arguments) throws NoDescriptionException, NoFromException, NoToException {
        String[] parts = arguments.split("/from", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            if (parts[0].trim().isEmpty()) {
                throw new NoDescriptionException("Event");
            }
            throw new NoFromException();
        }

        String[] timeParts = parts[1].split("/to", 2);

        if (timeParts.length < 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
            if (timeParts[0].trim().isEmpty()) {
                throw new NoFromException();
            }
            throw new NoToException();
        }

        Event newEvent = new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim());
        tasks.add(newEvent);
        System.out.printf("New event \"%s\" has been successfully eaten.\n", newEvent.description);
    }

    /**
     * Marks a task as done based on its index in the task list.
     *
     * @param index The index of the task to mark as done.
     */
    public void markTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            if (tasks.get(index).isDone) {
                System.out.println("Task already complete!");
            } else {
                tasks.get(index).markAsDone();
                System.out.println("Task " + tasks.get(index).description + " successfully marked as done.");
            }
        }
    }

    /**
     * Unmarks a task as undone based on its index in the task list.
     *
     * @param index The index of the task to unmark.
     */
    public void unmarkTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            if (!tasks.get(index).isDone) {
                System.out.println("Task already incomplete!");
            } else {
                tasks.get(index).markAsUndone();
                System.out.println("Task " + tasks.get(index).description + " successfully unmarked.");
            }
        }
    }

    /**
     * Lists all tasks in the task list.
     */
    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Nothing in my stomach. Feed me!");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the task list based on its index.
     *
     * @param index The index of the task to be removed.
     */
    public Task removeTask(int index) throws IndexOutOfBoundsException {
        if (index >= 0 && index < tasks.size()) {
            Task removed = tasks.get(index);
            tasks.remove(index);
            System.out.println("Task successfully removed.");
            return removed;
        } else {
            throw new IndexOutOfBoundsException("Can't find task of " + index);
        }
    }

    /**
     * Finds tasks in the list that match the given description.
     *
     * @param description The description to search for.
     * @return A list of tasks that match the given description.
     */
    public List<Task> findTasksByDescription(String description) {
        return tasks.stream()
                .filter(task -> task.description.toLowerCase().contains(description.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public boolean loadTasks() {
        try {
            tasks.clear();
            Path filePath = Paths.get("clonky", "tasks.txt");
            tasks.addAll(TaskWriter.LoadTasks(filePath.toString()));
            return true;
        } catch (IOException e) {
            System.out.println("File could not be read!");
        } catch (InvalidTaskFormatException | DateTimeParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void saveTasks(String filePath) {
        String path = (filePath == null || filePath.isEmpty())
                ? Paths.get("clonky", "tasks.txt").toString()
                : filePath;
        try {
            TaskWriter.saveTasks(tasks, path);
            System.out.printf("Tasks successfully saved at %s, Yippee!!!! >o<\n", path);
        } catch (IOException e) {
            // Handle exception (log, retry, etc.)
            System.err.println("Error occurred while saving tasks: " + e.getMessage());
        }
    }

    public int getSize() {
        return tasks.size();
    }
}
