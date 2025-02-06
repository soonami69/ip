package clonky.tasks;

import clonky.exceptions.*;
import clonky.response.Mood;
import clonky.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    void testAddTodo() throws NoDescriptionException {
        Response response = taskList.addTodo("Buy groceries");

        assertEquals(1, taskList.getSize());
        assertEquals("New todo \"Buy groceries\" has been successfully eaten.\n", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(new Color(138, 206, 0), response.getColor());
    }

    @Test
    void testAddTodoWithoutDescriptionThrowsException() {
        assertThrows(NoDescriptionException.class, () -> taskList.addTodo(" "));
    }

    @Test
    void testAddDeadline() throws NoDescriptionException, NoByException {
        Response response = taskList.addDeadline("Submit report /by 2025-02-10");

        assertEquals(1, taskList.getSize());
        assertEquals("New deadline \"Submit report\" has been successfully eaten.\n", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(new Color(255, 116, 108), response.getColor());
    }

    @Test
    void testAddDeadlineWithoutByThrowsException() {
        assertThrows(NoByException.class, () -> taskList.addDeadline("Submit report"));
    }

    @Test
    void testAddEvent() throws NoDescriptionException, NoFromException, NoToException {
        Response response = taskList.addEvent("Concert /from 2025-02-01 /to 2025-02-02");

        assertEquals(1, taskList.getSize());
        assertEquals("New event \"Concert\" has been successfully eaten.\n", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(new Color(167, 199, 231), response.getColor());
    }

    @Test
    void testAddEventWithoutFromThrowsException() {
        assertThrows(NoFromException.class, () -> taskList.addEvent("Concert /to 2025-02-02"));
    }

    @Test
    void testMarkTask() throws NoDescriptionException {
        taskList.addTodo("Buy groceries");
        Response response = taskList.markTask(0);

        assertEquals("Task Buy groceries successfully marked as done.", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(new Color(128, 239, 128), response.getColor());
    }

    @Test
    void testUnmarkTask() throws NoDescriptionException {
        taskList.addTodo("Buy groceries");
        taskList.markTask(0);
        Response response = taskList.unmarkTask(0);

        assertEquals("Task Buy groceries successfully unmarked.", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
    }

    @Test
    void testFindTasksByDescription() throws NoDescriptionException {
        taskList.addTodo("Buy milk");
        taskList.addTodo("Buy eggs");
        List<Task> found = taskList.findTasksByDescription("Buy");

        assertEquals(2, found.size());
    }
}
