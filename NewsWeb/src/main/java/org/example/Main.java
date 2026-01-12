package org.example;

import org.example.dao.DBConnection;
import org.example.service.Impl.DBServiceImpl;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //Kiểm tra đã có dữ liệu trong db chưa để kéo về
        Dotenv dotenv = Dotenv.load();
        try (Connection connection = DBConnection.getConnection()) {
            DBServiceImpl dbService = new DBServiceImpl();
            dbService.importDataFromJsonFileToDatabase(dotenv.get("DB_JSON_URL"), connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}