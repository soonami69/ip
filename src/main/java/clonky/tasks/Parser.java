package clonky.tasks;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Task> tasks = new ArrayList<>();

    public void addTodo(String description) throws NoDescriptionException {
        if (description == null || description.trim().isEmpty()) {
            throw new NoDescriptionException("Todo");
        }
        Todo newTodo = new Todo(description.trim());
        tasks.add(newTodo);
        System.out.printf("New todo \"%s\" has been successfully eaten.\n", newTodo.description);
        saveTasks();
        printTaskCount();
    }

    public void addDeadline(String arguments) throws NoDescriptionException, NoByException {
        String[] parts = arguments.split("/by", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            if (parts[0].trim().isEmpty()) {
                throw new NoDescriptionException("Deadline");
            }
            throw new NoByException();
        }
        try {
            Deadline newDeadline = new Deadline(parts[0].trim(), parts[1].trim());
            tasks.add(newDeadline);
            System.out.printf("New deadline \"%s\" has been successfully eaten.\n", newDeadline.description);
            saveTasks();
            printTaskCount();
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Try YYYY-MM-DD");
        }
    }

    public void addEvent(String arguments) throws NoFromException, NoToException, NoDescriptionException {
        String[] parts = arguments.split("/from", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            if (parts[0].trim().isEmpty()) {
                throw new NoDescriptionException("Event");
            }
            throw new NoFromException();
        }

        String[] timeParts = parts[1].split("/to", 2);

        // Ensure /to exists and both start and end times are non-empty
        if (timeParts.length < 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
            if (timeParts[0].trim().isEmpty()) {
                throw new NoFromException();
            }
            throw new NoToException();
        }

        try {
            Event newEvent = new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim());
            tasks.add(newEvent);
            System.out.printf("New event \"%s\" has been successfully eaten.\n", newEvent.description);
            saveTasks();
            printTaskCount();
        } catch (DateTimeParseException e){
            System.out.println("Invalid date format! Try DD-MM-YYYY");
        }
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
            saveTasks();
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
            saveTasks();
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
        saveTasks();
        System.out.println("Task successfully ANNIHILATED :3");
    }

    private void saveTasks() {
        Path filePath = Paths.get("clonky", "tasks.txt");
        String path = filePath.toString();
        try {
            TaskWriter.saveTasks(tasks, path);
        } catch (IOException e) {
            // Handle exception (log, retry, etc.)
            System.err.println("Error occurred while saving tasks: " + e.getMessage());
        }
    }

    public void saveTasks(String filePath) {
        String path = (filePath == null || filePath.isEmpty()) ? "./clonky/tasks.txt" : filePath;
        try {
            TaskWriter.saveTasks(tasks, path);
            System.out.printf("Tasks successfully saved at %s, Yippee!!!! >o<\n", path);
        } catch (IOException e) {
            // Handle exception (log, retry, etc.)
            System.err.println("Error occurred while saving tasks: " + e.getMessage());
        }
    }

    public boolean loadTasks(String filePath) {
        try {
            tasks.clear();
            tasks.addAll(TaskWriter.LoadTasks(filePath));
            return true;
        } catch (IOException e){
            System.out.println("File could not be read!");
        } catch (InvalidTaskFormatException | DateTimeParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean loadTasks() {
        try {
            tasks.clear();
            Path filePath = Paths.get("clonky", "tasks.txt");
            tasks.addAll(TaskWriter.LoadTasks(filePath.toString()));
            return true;
        } catch (IOException e){
            System.out.println("File could not be read!");
        } catch (InvalidTaskFormatException | DateTimeParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void printTaskCount() {
        System.out.printf("Now I have %d tasks in my tummy!\n", tasks.size());
    }
}
