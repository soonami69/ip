package clonky.tasks;

/**
 * General class for a task. Contains only a description and a completion status
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected int priority;

    /**
     * Initializes a task with a particular description.
     * @param description The description for the task.
     * @param priority The priority of the task.
     */
    public Task(String description, int priority) {
        assert description != null;
        this.description = description;
        this.isDone = false;
        this.priority = priority;
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
        return String.format("[%s] (Priority: %d) %s", this.getStatusIcon(), this.priority, this.description);
    }

    /**
     * Sets the priority of the task.
     * @param priority The new priority of the task.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
