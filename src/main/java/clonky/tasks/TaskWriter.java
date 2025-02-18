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

import clonky.exceptions.InvalidTaskFormatException;

/**
 * The {@code TaskWriter} class provides utility methods for saving and loading tasks to and from a file.
 * It supports parsing and reconstructing {@code Todo}, {@code Deadline}, and {@code Event} tasks with priority.
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
    public static List<Task> loadTasks(String filePath) throws IOException, InvalidTaskFormatException {
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
        if (line.length() < 6 || (line.charAt(4) != 'X' && line.charAt(4) != ' ')) {
            throw new InvalidTaskFormatException("Invalid task completion status in line: " + line);
        }
        return line.charAt(4) == 'X';
    }

    /**
     * Extracts the priority from the task string.
     *
     * @param line The task string.
     * @return The priority as an integer.
     * @throws InvalidTaskFormatException If the priority is not valid.
     */
    private static int extractPriority(String line) throws InvalidTaskFormatException {
        // Find the index of "(Priority: " to locate the start of the priority section.
        int priorityStartIndex = line.indexOf("(Priority: ");
        if (priorityStartIndex == -1) {
            throw new InvalidTaskFormatException("Priority not found in task: " + line);
        }

        // Extract the priority value, which should be immediately after "(Priority: ".
        int priorityEndIndex = line.indexOf(")", priorityStartIndex);
        if (priorityEndIndex == -1) {
            throw new InvalidTaskFormatException("Closing parenthesis for priority not found in task: " + line);
        }

        // Extract the priority string between "(Priority: " and the closing parenthesis.
        String priorityString = line.substring(priorityStartIndex + 11,
                priorityEndIndex).trim(); // +11 to skip "(Priority: "

        try {
            return Integer.parseInt(priorityString); // Parse the priority as an integer.
        } catch (NumberFormatException e) {
            throw new InvalidTaskFormatException("Invalid priority value in task: " + line);
        }
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
        int priority = extractPriority(line);

        // Find the position of the priority section's closing parenthesis
        int priorityEndIndex = line.indexOf(')', line.indexOf("(Priority: "));
        if (priorityEndIndex == -1) {
            throw new InvalidTaskFormatException("Invalid priority format in task: " + line);
        }

        // The description starts after the closing parenthesis and any following space
        String description = line.substring(priorityEndIndex + 2)
                .trim(); // Skip the space after the closing parenthesis

        Todo todo = new Todo(description, priority);
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
        int priority = extractPriority(line);
        int byIndex = line.indexOf("(by: ");
        if (byIndex == -1) {
            throw new InvalidTaskFormatException("Missing '(by: date)' in deadline task: " + line);
        }

        // Extract description after the priority section (Priority: X)
        int priorityEndIndex = line.indexOf(')', line.indexOf("(Priority: "));
        if (priorityEndIndex == -1) {
            throw new InvalidTaskFormatException("Invalid priority format in task: " + line);
        }

        // The description starts after the closing parenthesis and any following space
        String description = line.substring(priorityEndIndex + 2, byIndex).trim();

        // Extract the deadline date, which comes after the "(by: "
        String deadlineDate = line.substring(byIndex + 5, line.length() - 1).trim();
        LocalDate date = LocalDate.parse(deadlineDate, formatter);
        deadlineDate = date.toString();

        // Create and return the Deadline object
        Deadline deadline = new Deadline(description, deadlineDate, priority);
        if (isDone) {
            deadline.markAsDone();
        }
        return deadline;
    }

    /**
     * Parses an event task from a given line.
     * @param line The event task line from the file.
     * @return The parsed {@code Event} object.
     * @throws InvalidTaskFormatException If the format is invalid or missing required details.
     */
    private static Event parseEventTask(String line) throws InvalidTaskFormatException {
        boolean isDone = isTaskDone(line);
        int priority = extractPriority(line);
        int fromIndex = line.indexOf("(from ");
        if (fromIndex == -1) {
            throw new InvalidTaskFormatException("Missing '(from: start to end)' in event task: " + line);
        }

        // Extract description after the priority section (Priority: X)
        int priorityEndIndex = line.indexOf(')', line.indexOf("(Priority: "));
        if (priorityEndIndex == -1) {
            throw new InvalidTaskFormatException("Invalid priority format in task: " + line);
        }

        // The description starts after the closing parenthesis and any following space
        String description = line.substring(priorityEndIndex + 2, fromIndex).trim();

        Event event = getEvent(line, fromIndex, description, priority);
        if (isDone) {
            event.markAsDone();
        }
        return event;
    }
    /**
     * Extracts the event start and end dates from the event task string.
     *
     * @param line The event task string.
     * @param fromIndex The index where the "(from" section starts.
     * @param description The description of the event.
     * @param priority The priority of the event.
     * @return The {@code Event} object containing the extracted details.
     * @throws InvalidTaskFormatException If there is an error in extracting the event dates.
     */
    private static Event getEvent(String line, int fromIndex, String description,
                                  int priority) throws InvalidTaskFormatException {
        int toIndex = line.indexOf("to", fromIndex);
        if (toIndex == -1) {
            throw new InvalidTaskFormatException("Missing 'to' in event task: " + line);
        }

        // Extract start and end date of the event
        String startDate = line.substring(fromIndex + 6, toIndex).trim(); // Skip "(from " part
        String endDate = line.substring(toIndex + 3, line.length() - 1).trim(); // Skip "to " part

        // Parse the dates
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        // Return the event object
        return new Event(description, start.toString(), end.toString(), priority);
    }
}
