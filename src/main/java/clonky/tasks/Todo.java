package clonky.tasks;

/**
 * The {@code Todo} class represents a simple task without any time constraints.
 * It extends the {@code Task} class and only requires a description.
 */
public class Todo extends Task {

    /**
     * Constructs a new {@code Todo} task with the specified description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task, including its type indicator.
     *
     * @return A formatted string representing the todo task.
     */
    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
