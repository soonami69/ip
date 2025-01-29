package clonky.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code TaskWriter} class provides utility methods for saving and loading tasks to and from a file.
 * It supports parsing and reconstructing {@code Todo}, {@code Deadline}, and {@code Event} tasks.
 */
class TaskWriter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Saves the given list of tasks to the specified file path.
     * If the parent directory does not exist, it is created.
     *
     * @param tasks    The list of tasks to be saved.
     * @param filePath The file path where tasks should be stored.
     * @throws IOException If an error occurs while writing to the file.
     */
    public static void saveTasks(List<Task> tasks, String filePath) throws IOException {
        File file = new File(filePath);
        File parentFolder = file.getParentFile(); // Get parent directory

        // Ensure the directory exists
        if (parentFolder != null && !parentFolder.exists()) {
            boolean created = parentFolder.mkdirs(); // Create directories
            if (created) {
                System.out.println("Directory created at: " + parentFolder.getAbsolutePath());
            }
        }

        // Write tasks to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Loads tasks from the specified file path and reconstructs them into a list of {@code Task} objects.
     *
     * @param filePath The file path from which tasks should be loaded.
     * @return A list of tasks parsed from the file.
     * @throws IOException                If an error occurs while reading the file.
     * @throws InvalidTaskFormatException If the file contains an unrecognized task format.
     */
    public static List<Task> LoadTasks(String filePath) throws IOException, InvalidTaskFormatException {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    /**
     * Parses a line from the task file and converts it into a corresponding {@code Task} object.
     *
     * @param line The line to be parsed.
     * @return The parsed {@code Task} object.
     * @throws InvalidTaskFormatException If the line format is unrecognized.
     */
    private static Task parseTask(String line) throws InvalidTaskFormatException {
        if (line.startsWith("[T]")) {
            return parseTodoTask(line);
        } else if (line.startsWith("[D]")) {
            return parseDeadlineTask(line);
        } else if (line.startsWith("[E]")) {
            return parseEventTask(line);
        }
        throw new InvalidTaskFormatException("Task format: " + line + " is not recognized.");
    }

    /**
     * Determines if a task is marked as done based on the task string format.
     *
     * @param line The task string from the file.
     * @return {@code true} if the task is marked as done, {@code false} otherwise.
     * @throws InvalidTaskFormatException If the task completion status is invalid.
     */
    private static boolean isTaskDone(String line) throws InvalidTaskFormatException {
        if (line.length() < 5 || (line.charAt(4) != 'X' && line.charAt(4) != ' ')) {
            throw new InvalidTaskFormatException("Invalid task completion status in line: " + line);
        }
        return line.charAt(4) == 'X';
    }

    /**
     * Parses a todo task from a given line.
     *
     * @param line The todo task line from the file.
     * @return The parsed {@code Todo} object.
     * @throws InvalidTaskFormatException If the format is invalid.
     */
    private static Todo parseTodoTask(String line) throws InvalidTaskFormatException {
        boolean isDone = isTaskDone(line);
        String description = line.substring(7);
        Todo todo = new Todo(description);
        if (isDone) {
            todo.markAsDone();
        }
        return todo;
    }

    /**
     * Parses a deadline task from a given line.
     *
     * @param line The deadline task line from the file.
     * @return The parsed {@code Deadline} object.
     * @throws InvalidTaskFormatException If the format is invalid or missing required details.
     */
    private static Deadline parseDeadlineTask(String line) throws InvalidTaskFormatException {
        boolean isDone = isTaskDone(line);
        int byIndex = line.indexOf("(by: ");
        if (byIndex == -1) throw new InvalidTaskFormatException("Missing '(by: date)' in deadline task: " + line);
        String description = line.substring(7, byIndex).trim();
        String deadlineDate = line.substring(byIndex + 5, line.length() - 1).trim();
        LocalDate date = LocalDate.parse(deadlineDate, formatter);
        deadlineDate = date.toString();
        Deadline deadline = new Deadline(description, deadlineDate);
        if (isDone) {
            deadline.markAsDone();
        }
        return deadline;
    }

    /**
     * Parses an event task from a given line.
     *
     * @param line The event task line from the file.
     * @return The parsed {@code Event} object.
     * @throws InvalidTaskFormatException If the format is invalid or missing required details.
     */
    private static Event parseEventTask(String line) throws InvalidTaskFormatException {
        boolean isDone = isTaskDone(line);
        int fromIndex = line.indexOf("(from ");
        if (fromIndex == -1)
            throw new InvalidTaskFormatException("Missing '(from: start to end)' in event task: " + line);
        String description = line.substring(7, fromIndex).trim();
        int toIndex = line.indexOf("to", fromIndex);
        if (toIndex == -1) throw new InvalidTaskFormatException("Missing 'to' in event task: " + line);
        String startDate = line.substring(fromIndex + 6, toIndex).trim();
        String endDate = line.substring(toIndex + 3, line.length() - 1).trim();
        LocalDate date = LocalDate.parse(startDate, formatter);
        startDate = date.toString();
        date = LocalDate.parse(endDate, formatter);
        endDate = date.toString();
        Event event = new Event(description, startDate, endDate);
        if (isDone) {
            event.markAsDone();
        }
        return event;
    }
}
