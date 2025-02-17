package clonky.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clonky.exceptions.NoByException;
import clonky.exceptions.NoDescriptionException;
import clonky.response.Mood;
import clonky.response.Response;

class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    void testAddTodo() throws NoDescriptionException {
        Response response = taskList.addTodo("2 Buy groceries");

        assertEquals(1, taskList.getSize());
        assertEquals("New todo \"Buy groceries\" with priority 2 has been successfully eaten.\n", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(new Color(138, 206, 0), response.getColor());
    }

    @Test
    void testAddTodoWithoutDescriptionThrowsException() {
        assertThrows(NoDescriptionException.class, () -> taskList.addTodo("2 "));
    }

    @Test
    void testAddDeadline() throws NoDescriptionException, NoByException {
        Response response = taskList.addDeadline("1 Submit report /by 2025-02-10");

        assertEquals(1, taskList.getSize());
        assertEquals("New deadline \"Submit report\" with priority 1 "
                        + "has been successfully eaten.\n",
                response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(new Color(255, 116, 108), response.getColor());
    }

    @Test
    void testFindTasksByDescription() throws NoDescriptionException {
        taskList.addTodo("1 Buy milk");
        taskList.addTodo("1 Buy eggs");
        List<Task> found = taskList.findTasksByDescription("Buy");

        assertEquals(2, found.size());
    }
}
