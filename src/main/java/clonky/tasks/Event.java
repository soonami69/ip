package clonky.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The {@code Event} class represents a task that occurs within a specific time frame.
 * It extends the {@code Task} class and stores {@code LocalDate} values for the start and end dates.
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Constructs a new {@code Event} task with a description, start date, and end date.
     *
     * @param description The description of the event.
     * @param from The start date of the event in {@code YYYY-MM-DD} format.
     * @param to The end date of the event in {@code YYYY-MM-DD} format.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDate.parse(from);
        this.to = LocalDate.parse(to);
    }

    /**
     * Returns a string representation of the event task, including its description,
     * start date, and end date formatted as "MMM d yyyy".
     *
     * @return A formatted string representing the event task.
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from %s to %s)", super.toString(),
                this.from.format(DateTimeFormatter.ofPattern("MMM d yyyy")),
                this.to.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }
}
