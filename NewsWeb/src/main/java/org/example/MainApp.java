package org.example;

import org.example.DB.AddContent;
import org.example.DB.CheckImport;
import org.example.DB.CreateContentNewsJSON;
import org.example.RunFX.Launcher;

public class MainApp {

    public static void main(String[] args) {

        CheckImport checkImport = new CheckImport();
        checkImport.checkImport();

        AddContent.importFromJson("src/main/resources/content.json");
        CreateContentNewsJSON.exportToJson("src/main/resources/content.json");

        // gọi JavaFX
        Launcher.main(args);
    }
}