package clonky.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private Parser parser;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        parser = new Parser();
        System.setOut(new PrintStream(outputStream)); // Capture console output
    }

    @Test
    void testAddTodo_ValidInput() throws NoDescriptionException {
        parser.addTodo("Buy groceries");
        String output = outputStream.toString();
        assertTrue(output.contains("New todo \"Buy groceries\" has been successfully eaten."));
    }

    @Test
    void testAddTodo_EmptyDescription_ThrowsException() {
        Exception exception = assertThrows(NoDescriptionException.class, () -> parser.addTodo(""));
        assertEquals("Hey, you didn't give a description for this Todo", exception.getMessage());
    }

    @Test
    void testAddDeadline_ValidInput() throws NoDescriptionException, NoByException {
        parser.addDeadline("Submit assignment /by 2024-06-30");
        String output = outputStream.toString();
        assertTrue(output.contains("New deadline \"Submit assignment\" has been successfully eaten."));
    }

    @Test
    void testAddDeadline_MissingBy_ThrowsException() {
        Exception exception = assertThrows(NoByException.class, () -> parser.addDeadline("Submit assignment"));
        assertEquals("Specify a deadline for your... well, deadline with \"/by {date}\"", exception.getMessage());
    }

    @Test
    void testAddEvent_MissingFrom_ThrowsException() {
        Exception exception = assertThrows(NoFromException.class, () -> parser.addEvent("Dance battle /to 2:00PM"));
        assertEquals("Specify a starting date for your event with \"/from {date}\"", exception.getMessage());
    }

    @Test
    void testAddEvent_ValidInput() throws NoDescriptionException, NoFromException, NoToException {
        parser.addEvent("Eat chicken rice /from 2019-02-02 /to 2020-03-20");
        String output = outputStream.toString();
        assertTrue(output.contains("New event \"Eat chicken rice\" has been successfully eaten."));
    }

    @Test
    void testListTasks_EmptyList() {
        parser.listTasks();
        String output = outputStream.toString();
        assertTrue(output.contains("Nothing in my stomach. Feed me!"));
    }

    @Test
    void testMarkTask_InvalidIndex() {
        parser.markTask("999"); // No tasks exist yet
        String output = outputStream.toString();
        assertTrue(output.contains("Please provide a number between 1 and 0!"));
    }

    @Test
    void testRemoveTask_InvalidIndex() {
        parser.removeTask("999");
        String output = outputStream.toString();
        assertTrue(output.contains("Please provide a number between 1 and 0!"));
    }

    @Test
    void testSaveTasks(@TempDir Path tempDir) throws IOException {
        Path testFilePath = tempDir.resolve("test_tasks.txt");
        parser.saveTasks(testFilePath.toString());

        assertTrue(testFilePath.toFile().exists());
    }
}
