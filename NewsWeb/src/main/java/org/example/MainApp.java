package org.example;

import org.example.DB.AddTables.AddFullTables;
import org.example.DB.CheckImport;
import org.example.DB.CreateJSON.CreateFullTables;
import org.example.RunFX.Launcher;

public class MainApp {

    public static void main(String[] args) {

        CheckImport checkImport = new CheckImport();
        checkImport.checkImport();

        AddFullTables.addTable();
        CreateFullTables.createTables();

        // gọi JavaFX
        Launcher.main(args);
    }
}