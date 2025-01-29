package clonky.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    void addTask_shouldIncreaseSize() throws NoDescriptionException {
        taskList.addTodo("Buy milk");
        assertEquals(1, taskList.getSize(), "Task list size should be 1 after adding a task.");
    }

    @Test
    void removeTask_shouldDecreaseSize() throws NoDescriptionException {
        taskList.addTodo("Buy milk");
        taskList.addTodo("Walk the dog");

        Task removedTask = taskList.removeTask(0);
        assertEquals("Buy milk", removedTask.description, "Removed task should be 'Buy milk'.");
        assertEquals(1, taskList.getSize(), "Task list size should be 1 after removing a task.");
    }

    @Test
    void removeTask_invalidIndex_shouldThrowException() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.removeTask(0), "Removing from empty list should throw an exception.");
    }

    @Test
    void getTask_validIndex_shouldReturnCorrectTask() throws NoDescriptionException {
        taskList.addTodo("Finish homework");
        Task retrievedTask = taskList.getTask(0);
        assertEquals("Finish homework", retrievedTask.description, "Retrieved task should be 'Finish homework'.");
    }

    @Test
    void getTask_invalidIndex_shouldThrowException() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.getTask(0), "Retrieving from empty list should throw an exception.");
    }

    @Test
    void findTasks_shouldReturnMatchingTasks() {
        taskList.addTask(new Todo("Buy groceries"));
        taskList.addTask(new Todo("Finish project"));
        taskList.addTask(new Todo("Groceries for party"));

        List<Task> results = taskList.findTasksByDescription("groceries");
        assertEquals(2, results.size(), "Should find 2 tasks containing 'groceries'.");
    }

    @Test
    void findTasks_noMatch_shouldReturnEmptyList() {
        taskList.addTask(new Todo("Clean the house"));
        List<Task> results = taskList.findTasksByDescription("homework");
        assertTrue(results.isEmpty(), "Should return an empty list if no tasks match.");
    }

    @Test
    void listTasks_emptyList_shouldPrintMessage() {
        taskList.listTasks(); // Manually check the console output for "Nothing in my stomach. Feed me!"
    }

    @Test
    void listTasks_nonEmptyList_shouldPrintTasks() {
        taskList.addTask(new Todo("Buy groceries"));
        taskList.addTask(new Todo("Finish project"));

        taskList.listTasks(); // Manually check the console output
    }
}
