package clonky.response;

import org.junit.jupiter.api.Test;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void testGetText() {
        Response response = new Response("Hello, Clonky!", Mood.HAPPY, Color.YELLOW);
        assertEquals("Hello, Clonky!", response.getText());
    }

    @Test
    void testGetMood() {
        Response response = new Response("I'm feeling good!", Mood.HAPPY, Color.YELLOW);
        assertEquals(Mood.HAPPY, response.getMood());
    }

    @Test
    void testGetColor() {
        Color testColor = Color.GREEN;
        Response response = new Response("Custom color test", Mood.CHAOTIC, testColor);
        assertEquals(testColor, response.getColor());
    }

    @Test
    void testGetColorFromMood() {
        assertEquals(Color.BLUE, Response.getColorFromMood(Mood.SAD));
        assertEquals(Color.RED, Response.getColorFromMood(Mood.ANGRY));
        assertEquals(Color.YELLOW, Response.getColorFromMood(Mood.HAPPY));
    }

    @Test
    void testGetColorFromMood_ChaoticRandomness() {
        Color color1 = Response.getColorFromMood(Mood.CHAOTIC);
        Color color2 = Response.getColorFromMood(Mood.CHAOTIC);
        // Random colors should not always be equal
        assertNotEquals(color1, color2, "CHAOTIC mood should return different colors.");
    }

    @Test
    void testGetImageFromMood() {
        assertEquals("/images/clonkySad.jpg", Response.getImageFromMood(Mood.SAD));
        assertEquals("/images/clonkyAngry.jpg", Response.getImageFromMood(Mood.ANGRY));
        assertEquals("/images/clonkyHappy.jpg", Response.getImageFromMood(Mood.HAPPY));
        assertEquals("/images/clonkyChaotic.jpg", Response.getImageFromMood(Mood.CHAOTIC));
    }

    @Test
    void testToString() {
        Response response = new Response("Test message", Mood.HAPPY, Color.YELLOW);
        String expected = "Response:\nText: Test message\nMood: HAPPY\nColor: java.awt.Color[r=255,g=255,b=0]";
        assertTrue(response.toString().contains("Text: Test message"));
        assertTrue(response.toString().contains("Mood: HAPPY"));
        assertTrue(response.toString().contains("Color:"));
    }
}
