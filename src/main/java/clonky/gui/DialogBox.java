package clonky.gui;

import java.awt.Color;
import java.io.IOException;

import clonky.response.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * The Controller class for the Dialog Box UI element.
 * Takes in Responses and displays the Text, Mood and Color.
 */
public class DialogBox extends HBox {
    private static MainWindow mainWindow; // instance of mainWindow to load images from
    private static Image userImage;
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Initializes the Dialogue Box
     * @param r The response to be displayed
     * @param i The Image to be displayed
     */
    public DialogBox(Response r, Image i) {
        assert r != null;
        assert i != null;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource(
                    "/clonky/gui/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String moodColorString = colorToString(Response.getColorFromMood(r.getMood()));
        dialog.setStyle(String.format("-fx-background-color: linear-gradient(to bottom right, %s, %s"
                        + "); -fx-border-color: %s", colorToString(r.getColor()),
                colorToString(r.getColor().brighter()),
                moodColorString));
        dialog.setText(r.getText());
        displayPicture.setImage(i);
        // Making rounded images
        Circle clip = new Circle(50, 50, 50);
        displayPicture.setClip(clip);
    }

    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        dialog.getStyleClass().add("reply-label");
        this.getChildren().setAll(tmp);
    }

    public static DialogBox getUserDialog(Response r) {
        assert r != null;
        return new DialogBox(r, userImage);
    }

    public static DialogBox getClonkyDialog(Response r) {
        assert r != null;
        Image i = new Image(mainWindow.getClass().getResourceAsStream(
                Response.getImageFromMood(r.getMood())));
        var db = new DialogBox(r, i);
        db.flip();
        return db;
    }

    private String colorToString(Color color) {
        return String.format("rgb(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static void setMainWindow(MainWindow mw) {
        DialogBox.mainWindow = mw;
        userImage = new Image(mw.getClass().getResourceAsStream("/images/userProfile"
                + ".png"));
    }
}
