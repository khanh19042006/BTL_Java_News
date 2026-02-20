package org.example.DB;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.dao.DBConnection;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CreateContentNewsJSON {

    public static void exportToJson(String outputPath) {

        String sql = """
                SELECT id, content
                FROM news
                WHERE content IS NOT NULL
                AND content <> ''
                """;

        List<NewsContent> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String content = rs.getString("content");

                list.add(new NewsContent(id, content));
            }

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            try (FileWriter writer = new FileWriter(outputPath)) {
                gson.toJson(list, writer);
            }

            System.out.println("Xuất JSON thành công: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class NewsContent {
        private String id;
        private String content;

        public NewsContent(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }
}
