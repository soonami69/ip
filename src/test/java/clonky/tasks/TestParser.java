package clonky.tasks;

import java.awt.Color;

import clonky.exceptions.NoByException;
import clonky.exceptions.NoDescriptionException;
import clonky.exceptions.NoFromException;
import clonky.exceptions.NoToException;
import clonky.response.Mood;
import clonky.response.Response;

/**
 * A class that emulates the behavior of Parser for testing.
 */
public class TestParser extends Parser {
    @Override
    public Response addTodo(String description) throws NoDescriptionException {
        if (description.isEmpty()) {
            throw new NoDescriptionException("");
        }
        return new Response("Todo added!", Mood.HAPPY, Color.GREEN);
    }

    @Override
    public Response addDeadline(String arguments) throws NoDescriptionException, NoByException {
        if (arguments.isEmpty()) {
            throw new NoDescriptionException("");
        }
        if (!arguments.contains("/by")) {
            throw new NoByException();
        }
        return new Response("Deadline added!", Mood.HAPPY, Color.GREEN);
    }

    @Override
    public Response addEvent(String arguments) throws NoDescriptionException, NoFromException, NoToException {
        if (arguments.isEmpty()) {
            throw new NoDescriptionException("");
        }
        if (!arguments.contains("/from")) {
            throw new NoFromException();
        }
        if (!arguments.contains("/to")) {
            throw new NoToException();
        }
        return new Response("Event added!", Mood.HAPPY, Color.GREEN);
    }

    @Override
    public Response welcome() {
        return new Response("Welcome to Clonky!", Mood.HAPPY, Color.GREEN);
    }

    @Override
    public Response loadTasks() {
        return new Response("Tasks loaded successfully.", Mood.HAPPY, Color.GREEN);
    }
}
