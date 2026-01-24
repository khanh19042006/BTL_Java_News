package org.example;

import org.example.DB.CheckImport;
import org.example.RunFX.Launcher;

public class MainApp {

    public static void main(String[] args) {

        CheckImport checkImport = new CheckImport();
        checkImport.checkImport();

        // gọi JavaFX
        Launcher.main(args);
    }
}