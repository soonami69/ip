package clonky.tasks;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import clonky.exceptions.InvalidTaskFormatException;
import clonky.exceptions.NoByException;
import clonky.exceptions.NoDescriptionException;
import clonky.exceptions.NoFromException;
import clonky.exceptions.NoToException;
import clonky.response.Mood;
import clonky.response.Response;

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
    public Response addTodo(String description) throws NoDescriptionException {
        if (description == null || description.trim().isEmpty()) {
            throw new NoDescriptionException("Todo");
        }
        Todo newTodo = new Todo(description.trim());
        tasks.add(newTodo);
        String text = String.format("New todo \"%s\" has been successfully eaten.\n", newTodo.description);
        Mood mood = Mood.HAPPY;
        Color color = new Color(138, 206, 0);
        saveTasks("");
        return new Response(text, mood, color);
    }

    /**
     * Adds a deadline task to the task list.
     *
     * @param arguments The arguments containing the description and deadline.
     * @throws NoDescriptionException If the description is empty.
     * @throws NoByException         If the '/by' part is missing.
     */
    public Response addDeadline(String arguments) throws NoDescriptionException, NoByException {
        String[] parts = arguments.split("/by", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            if (parts[0].trim().isEmpty()) {
                throw new NoDescriptionException("Deadline");
            }
            throw new NoByException();
        }
        Deadline newDeadline = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(newDeadline);
        String text = String.format("New deadline \"%s\" has been successfully eaten.\n", newDeadline.description);
        Mood mood = Mood.HAPPY;
        Color color = new Color(255, 116, 108);
        saveTasks("");
        return new Response(text, mood, color);
    }

    /**
     * Adds an event task to the task list.
     *
     * @param arguments The arguments containing the description, start, and end dates.
     * @throws NoDescriptionException If the description is empty.
     * @throws NoFromException       If the '/from' part is missing.
     * @throws NoToException         If the '/to' part is missing.
     */
    public Response addEvent(String arguments) throws NoDescriptionException, NoFromException, NoToException {
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
        String text = String.format("New event \"%s\" has been successfully eaten.\n", newEvent.description);
        Mood mood = Mood.HAPPY;
        Color color = new Color(167, 199, 231);
        saveTasks("");
        return new Response(text, mood, color);
    }

    /**
     * Marks a task as done based on its index in the task list.
     *
     * @param index The index of the task to mark as done.
     */
    public Response markTask(int index) {
        String text = "";
        Mood mood = Mood.HAPPY;
        Color color = new Color(128, 239, 128);
        if (index >= 0 && index < tasks.size()) {
            if (tasks.get(index).isDone) {
                text = "Task already complete!";
                mood = Mood.SAD;
                color = new Color(255, 105, 97);
            } else {
                tasks.get(index).markAsDone();
                text = String.format("Task " + tasks.get(index).description
                        + " successfully marked as done.");
                saveTasks("");
            }
        }
        return new Response(text, mood, color);
    }

    /**
     * Unmarks a task as undone based on its index in the task list.
     *
     * @param index The index of the task to unmark.
     */
    public Response unmarkTask(int index) {
        String text = "";
        Mood mood = Mood.HAPPY;
        Color color = new Color(128, 239, 128);
        if (index >= 0 && index < tasks.size()) {
            if (!tasks.get(index).isDone) {
                text = "Task already incomplete!";
                mood = Mood.SAD;
                color = new Color(255, 105, 97);
            } else {
                tasks.get(index).markAsUndone();
                text = "Task " + tasks.get(index).description + " successfully unmarked.";
                saveTasks("");
            }
        }
        return new Response(text, mood, color);
    }

    /**
     * Lists all tasks in the task list.
     */
    public Response listTasks() {
        StringBuilder text = new StringBuilder();
        Mood mood = Mood.HAPPY;
        String color = "";
        if (tasks.isEmpty()) {
            text = new StringBuilder("Nothing in my stomach. Feed me!");
            mood = Mood.ANGRY;
            return new Response(text.toString(), mood, new Color(128, 239, 128));
        }
        for (int i = 0; i < tasks.size(); i++) {
            String taskString = (i + 1) + ". " + tasks.get(i) + "\n";
            text.append(taskString);
        }
        return new Response(text.toString(), mood, new Color(179, 235, 242));
    }

    /**
     * Removes a task from the task list based on its index.
     *
     * @param index The index of the task to be removed.
     */
    public Response removeTask(int index) throws IndexOutOfBoundsException {
        String text = "";
        Mood mood;
        if (index >= 0 && index < tasks.size()) {
            Task removed = tasks.get(index);
            tasks.remove(index);
            text = "Task successfully removed.\n" + removed.toString();
            mood = Mood.HAPPY;
            saveTasks("");
            return new Response(text, mood, new Color(128, 239, 128));
        } else {
            text = "Can't find task of " + index;
            mood = Mood.ANGRY;
            return new Response(text, mood, new Color(255, 116, 108));
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

    /**
     * Load tasks from /clonky/tasks.txt.
     * @return A Response that contains text that says if the loading has been successful
     *      or not.
     */
    public Response loadTasks() {
        String text;
        Mood mood;
        Color color;
        try {
            tasks.clear();
            Path filePath = Paths.get("clonky", "tasks.txt");
            tasks.addAll(TaskWriter.loadTasks(filePath.toString()));
            text = "Tasks loaded successfully!!!!";
            mood = Mood.HAPPY;
            color = new Color(179, 235, 242);
            return new Response(text, mood, color);
        } catch (IOException e) {
            text = "File could not be read!";
            mood = Mood.SAD;
            color = new Color(255, 116, 108);
            return new Response(text, mood, color);
        } catch (InvalidTaskFormatException | DateTimeParseException e) {
            text = e.getMessage();
            mood = Mood.ANGRY;
            color = Color.red;
            return new Response(text, mood, color);
        }
    }

    /**
     * Saves tasks to a certain filePath, or /clonky/tasks.txt if none is provided.
     * @param filePath The file path to save to.
     * @return A response detailing if the operation was successful or not.
     */
    public Response saveTasks(String filePath) {
        String text = "";
        Mood mood;
        Color color;
        String path = (filePath == null || filePath.isEmpty())
                ? Paths.get("clonky", "tasks.txt").toString()
                : filePath;
        try {
            TaskWriter.saveTasks(tasks, path);
            text = String.format("Tasks successfully saved at %s, Yippee!!!! >o<\n", path);
            mood = Mood.HAPPY;
            color = Color.green;
            return new Response(text, mood, color);
        } catch (IOException e) {
            // Handle exception (log, retry, etc.)
            text = "Error occurred while saving tasks: " + e.getMessage() + "\n";
            mood = Mood.SAD;
            color = Color.red;
            System.err.println(text);
            return new Response(text, mood, color);
        }
    }

    public int getSize() {
        return tasks.size();
    }
}
