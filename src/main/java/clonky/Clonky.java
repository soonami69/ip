package clonky;

import clonky.tasks.Parser;
import clonky.ui.UI;

import java.util.Scanner;

public class Clonky {
    private final UI ui;

    public Clonky(String filePath) {
        //TODO: Let filepath be configurable...
        ui = new UI(new Parser(), new Scanner(System.in));
    }

    public static void main(String[] args) {
        new Clonky("This isn't working yet").run();
    }

    public void run() {
        ui.startup();
    }
}
