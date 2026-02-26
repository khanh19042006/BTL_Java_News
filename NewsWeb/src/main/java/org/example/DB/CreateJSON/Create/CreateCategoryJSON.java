package org.example.DB.CreateJSON.Create;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.dao.DBConnection;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CreateCategoryJSON {

    public static void exportToJson(String outputPath) {

        String sql = "SELECT id, code, name FROM category";

        List<CategoryData> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new CategoryData(
                        rs.getString("id"),
                        rs.getString("code"),
                        rs.getString("name")
                ));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(outputPath)) {
                gson.toJson(list, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class CategoryData {
        String id, code, name;

        public CategoryData(String id, String code, String name) {
            this.id = id;
            this.code = code;
            this.name = name;
        }
    }
}