package clonky;

import java.io.IOException;
import java.util.Scanner;

import clonky.gui.MainWindow;
import clonky.handler.Handler;
import clonky.tasks.Parser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The main entry point for the Clonky application.
 * Clonky is a task manager that supports different types of tasks and user interactions.
 */
public class Clonky extends Application {
    private Handler handler;

    /**
     * Initializes Clonky with a specified file path for saving/loading tasks. Currently the
     * File path is hard-coded.
     */
    public Clonky() {
        //TODO: Let filepath be configurable...
        handler = new Handler(new Parser(), new Scanner(System.in));
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Clonky.class.getResource("/clonky/gui/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setTitle("Clonky");
            stage.getIcons().add(new Image(
                    Clonky.class.getResourceAsStream("/images/clonkyHappy.jpg")));
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setHandler(handler); // inject the Handler
            fxmlLoader.<MainWindow>getController().welcome(); // say hi and load tasks
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
