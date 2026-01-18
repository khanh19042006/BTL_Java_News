package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.example.dao.DBConnection;
import org.example.service.Impl.DBServiceImpl;

import io.github.cdimascio.dotenv.Dotenv;

import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/Login/login.fxml")
        );
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {

        //Kiểm tra đã có dữ liệu trong db chưa để kéo về
        Dotenv dotenv = Dotenv.load();
        try (Connection connection = DBConnection.getConnection()) {
            DBServiceImpl dbService = new DBServiceImpl();
            dbService.importDataFromJsonFileToDatabase(dotenv.get("DB_JSON_URL"), connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Chạy homepage
        launch();
    }
}