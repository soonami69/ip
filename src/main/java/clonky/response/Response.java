package clonky.response;

import java.awt.Color;
import java.util.Random;

/**
 * Class that encapsulates a response from the Handler class
 * Contains a text, mood, and colour for rendering of text boxes
 */
public class Response {
    private static final Random RANDOM = new Random();
    private final String text;
    private final Mood mood;
    private final Color color;

    /**
     * A class that encapsulates a response from Clonky.
     * @param text The text that Clonky has to say.
     * @param mood Clonky's mood.
     * @param color A color that the text book is to be displayed
     */
    public Response(String text, Mood mood, Color color) {
        this.text = text;
        this.mood = mood;
        this.color = color;
    }

    public String getText() {
        return this.text;
    }

    public Mood getMood() {
        return this.mood;
    }

    /**
     * Gets the stored color
     * @return The stored color. It should be in Hex Code.
     */
    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "Response:\n" + "Text: " + this.text
                + "\n" + "Mood: " + this.mood + "\n"
                + "Color: " + this.color;
    }

    public static Color getColorFromMood(Mood mood) {
        switch (mood) {
        case SAD:
            return Color.blue;
        case ANGRY:
            return Color.red;
        case HAPPY:
            return Color.yellow;
        case CHAOTIC:
            return new Color(RANDOM.nextFloat(), RANDOM.nextFloat(), RANDOM.nextFloat());
        default:
            return null;
        }
    }

    public static String getImageFromMood(Mood mood) {
        switch (mood) {
        case SAD:
            return "/images/clonkySad.jpg";
        case ANGRY:
            return "/images/clonkyAngry.jpg";
        case HAPPY:
            return "/images/clonkyHappy.jpg";
        case CHAOTIC:
            return "/images/clonkyChaotic.jpg";
        default:
            return null;
        }
    }
}
