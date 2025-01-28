package tasks;

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

class TaskWriter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
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

    private static boolean isTaskDone(String line) throws InvalidTaskFormatException {
        if (line.length() < 5 || (line.charAt(4) != 'X' && line.charAt(4) != ' ')) {
            throw new InvalidTaskFormatException("Invalid task completion status in line: " + line);
        }
        return line.charAt(4) == 'X';
    }

    private static Todo parseTodoTask(String line) throws InvalidTaskFormatException {
        boolean isDone = isTaskDone(line);
        String description = line.substring(7);
        Todo todo = new Todo(description);
        if (isDone) {
            todo.markAsDone();
        }
        return todo;
    }

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

    private static Event parseEventTask(String line) throws InvalidTaskFormatException {
        boolean isDone = isTaskDone(line);
        int fromIndex = line.indexOf("(from ");
        if (fromIndex == -1) throw new InvalidTaskFormatException("Missing '(from: start to end)' in event task: " + line);
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
