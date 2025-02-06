package clonky.handler;

import clonky.response.Mood;
import clonky.response.Response;
import clonky.tasks.TestParser;

import java.awt.*;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandlerTest {
    private Handler handler;

    @BeforeEach
    void setUp() {
        handler = new Handler(new TestParser(), new Scanner(System.in));
    }

    @Test
    void testStartReturnsWelcomeMessage() {
        Response response = handler.start();

        assertEquals("Welcome to Clonky!", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(Color.GREEN, response.getColor());
    }

    @Test
    void testLoadTasksReturnsSuccessMessage() {
        Response response = handler.loadTasks();

        assertEquals("Tasks loaded successfully.", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(Color.GREEN, response.getColor());
    }

    @Test
    void testGetResponseHandlesUnknownCommand() {
        Response response = handler.getResponse("unknownCommand");

        assertEquals("*embraces you gently* Hey, you know that {unknownCommand} is "
                + "not a recognized command, right?", response.getText());
        assertEquals(Mood.CHAOTIC, response.getMood());
        assertEquals(Color.WHITE, response.getColor());
    }

    @Test
    void testHandleCommand_todoWithValidArgs() {
        Response response = handler.getResponse("todo Buy milk");

        assertEquals("Todo added!", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(Color.GREEN, response.getColor());
    }

    @Test
    void testHandleCommand_todoWithNoDescription() {
        Response response = handler.getResponse("todo ");

        assertEquals(Mood.SAD, response.getMood());
        assertEquals(Color.RED, response.getColor());
    }

    @Test
    void testHandleCommand_deadlineWithValidArgs() {
        Response response = handler.getResponse("deadline Submit report /by 2025-02-10");

        assertEquals("Deadline added!", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(Color.GREEN, response.getColor());
    }

    @Test
    void testHandleCommand_deadlineWithNoBy() {
        Response response = handler.getResponse("deadline Submit report");

        assertEquals(Mood.SAD, response.getMood());
        assertEquals(Color.RED, response.getColor());
    }

    @Test
    void testHandleCommand_eventWithValidArgs() {
        Response response = handler.getResponse("event Concert /from 2025-02-01 /to 2025-02-02");

        assertEquals("Event added!", response.getText());
        assertEquals(Mood.HAPPY, response.getMood());
        assertEquals(Color.GREEN, response.getColor());
    }

    @Test
    void testHandleCommand_eventWithMissingFrom() {
        Response response = handler.getResponse("event Concert /to 2025-02-02");

        assertEquals(Mood.SAD, response.getMood());
        assertEquals(new Color(255, 116, 108), response.getColor());
    }
}
