package clonky.tasks;

/**
 * General class for a task. Contains only a description and a completion status
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Initializes a task with a particular description.
     * @param description The description for the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsUndone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
