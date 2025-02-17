package clonky.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The {@code Deadline} class represents a task that has a specific due date.
 * It extends the {@code Task} class and stores a {@code LocalDate} for the deadline.
 */
public class Deadline extends Task {
    private final LocalDate by;

    /**
     * Constructs a new {@code Deadline} task with a description and due date.
     *
     * @param description The description of the deadline task.
     * @param by The due date in {@code YYYY-MM-DD} format.
     */
    public Deadline(String description, String by, int priority) {
        super(description, priority);

        assert by != null;
        this.by = LocalDate.parse(by);
    }

    /**
     * Returns a string representation of the deadline task,
     * including its description and due date formatted as "MMM d yyyy".
     *
     * @return A formatted string representing the deadline task.
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(),
                this.by.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }
}
