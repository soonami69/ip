package clonky.gui;

import clonky.handler.Handler;
import clonky.response.Mood;
import clonky.response.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.awt.*;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Handler handler;
    @FXML
    public void initialize() {
        DialogBox.setMainWindow(this);
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Configures the handler to handle inputs
     */
    public void setHandler(Handler h) {
        handler = h;
    }

    /**
     * Tries to load tasks from file and greets user with a welcome message
     */
    public void welcome() {
        Response startResponse = handler.start();
        Response loadTasksResponse = handler.loadTasks();
        dialogContainer.getChildren().addAll(
                DialogBox.getClonkyDialog(startResponse),
                DialogBox.getClonkyDialog(loadTasksResponse)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        Response clonkyResponse = handler.getResponse(userInput.getText());
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(new Response(userText, Mood.HAPPY, Color.WHITE)),
                DialogBox.getClonkyDialog(clonkyResponse)
        );
        userInput.clear();
    }

}

