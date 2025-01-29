    package clonky;

    import clonky.tasks.Parser;
    import clonky.tasks.UnknownCommandException;
    import clonky.ui.UI;

    import java.util.Scanner;
    /**
     * The main entry point for the Clonky application.
     * Clonky is a task manager that supports different types of tasks and user interactions.
     */
    public class Clonky {
        private UI ui;

        /**
         * Initializes Clonky with a specified file path for saving/loading tasks. Currently the
         * File path is hard-coded.
         *
         * @param filePath The path where tasks are stored. (Currently not used in initialization)
         */
        public Clonky(String filePath) {
            //TODO: Let filepath be configurable...
            ui = new UI(new Parser(), new Scanner(System.in));
        }

        /**
         * Starts the Clonky task manager by invoking the UI.
         */
        public void run() {
            ui.startup();
        }

        /**
         * The main method, which starts the Clonky application.
         *
         * @param args Command-line arguments (not currently used)
         */
        public static void main(String[] args) {
            new Clonky("This isn't working yet").run();
        }
    }
