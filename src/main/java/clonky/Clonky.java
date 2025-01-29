package clonky;

import clonky.tasks.Parser;
import clonky.tasks.UnknownCommandException;
import clonky.ui.UI;

import java.util.Scanner;

public class Clonky {
    private UI ui;

    public Clonky(String filePath) {
        //TODO: Let filepath be configurable...
        ui = new UI(new Parser(), new Scanner(System.in));
    }

    public void run() {
        ui.startup();
    }

    public static void main(String[] args) {
        new Clonky("This isn't working yet").run();
    }
}
