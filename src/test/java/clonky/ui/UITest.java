package clonky.ui;

import clonky.tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UITest {
    private UI ui;
    private MockParser mockParser;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Use a custom mock parser
        mockParser = new MockParser();

        // Capture system output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Simulate user input
        String simulatedInput = "todo Buy milk\nbye\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Initialize UI with the mock parser and simulated input
        ui = new UI(mockParser, new Scanner(System.in));
    }

    @Test
    void testTodoCommand() {
        ui.startup();
        assertTrue(mockParser.todoCalled, "addTodo() should be called");
        assertTrue(mockParser.todoArgument.equals("Buy milk"), "Argument passed to addTodo() should match input");
    }

    @Test
    void testExitMessageOnBye() {
        ui.startup();
        String output = outputStream.toString();
        assertTrue(output.contains("Go away!"), "Exit message should be displayed");
    }
}

/**
 * Custom mock class for testing UI without Mockito.
 */
class MockParser extends Parser {
    boolean todoCalled = false;
    String todoArgument = "";

    @Override
    public void addTodo(String description) throws NoDescriptionException {
        todoCalled = true;
        todoArgument = description;
    }

    @Override
    public boolean loadTasks() {
        return true;
    }
}
