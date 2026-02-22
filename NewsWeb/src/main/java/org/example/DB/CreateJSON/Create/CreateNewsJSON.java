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

public class CreateNewsJSON {

    public static void exportToJson(String outputPath) {

        String sql = """
                SELECT id, link, headline, category,
                       short_description, authors, date,
                       views, content, thumbnail, author_id
                FROM news
                """;

        List<NewsData> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(new NewsData(
                        rs.getString("id"),
                        rs.getString("link"),
                        rs.getString("headline"),
                        rs.getString("category"),
                        rs.getString("short_description"),
                        rs.getString("authors"),
                        rs.getString("date"),
                        rs.getInt("views"),
                        rs.getString("content"),
                        rs.getString("thumbnail"),
                        rs.getString("author_id")
                ));
            }

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            try (FileWriter writer = new FileWriter(outputPath)) {
                gson.toJson(list, writer);
            }

            System.out.println("Export news thành công: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class NewsData {

        String id;
        String link;
        String headline;
        String category;
        String short_description;
        String authors;
        String date;
        int views;
        String content;
        String thumbnail;
        String author_id;

        public NewsData(String id, String link, String headline,
                        String category, String short_description,
                        String authors, String date, int views,
                        String content, String thumbnail, String author_id) {
            this.id = id;
            this.link = link;
            this.headline = headline;
            this.category = category;
            this.short_description = short_description;
            this.authors = authors;
            this.date = date;
            this.views = views;
            this.content = content;
            this.thumbnail = thumbnail;
            this.author_id = author_id;
        }
    }
}