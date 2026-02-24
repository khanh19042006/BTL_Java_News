package org.example.DB.AddTables.Add;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.dao.DBConnection;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

public class AddNews {

    public static void importFromJson(String jsonPath) {

        String selectSql = """
                SELECT link, headline, category, short_description,
                       authors, date, views, content, thumbnail, author_id
                FROM news
                WHERE id = ?
                """;

        String updateSql = """
                UPDATE news
                SET link = ?,
                    headline = ?,
                    category = ?,
                    short_description = ?,
                    authors = ?,
                    date = ?,
                    views = ?,
                    content = ?,
                    thumbnail = ?,
                    author_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<NewsData>>() {}.getType();
            List<NewsData> list = gson.fromJson(reader, listType);

            int updated = 0;
            int skipped = 0;

            for (NewsData item : list) {

                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {

                    boolean isDifferent =
                            !Objects.equals(rs.getString("link"), item.getLink()) ||
                                    !Objects.equals(rs.getString("headline"), item.getHeadline()) ||
                                    !Objects.equals(rs.getString("category"), item.getCategory()) ||
                                    !Objects.equals(rs.getString("short_description"), item.getShort_description()) ||
                                    !Objects.equals(rs.getString("authors"), item.getAuthors()) ||
                                    !Objects.equals(rs.getString("date"), item.getDate()) ||
                                    rs.getInt("views") != item.getViews() ||
                                    !Objects.equals(rs.getString("content"), item.getContent()) ||
                                    !Objects.equals(rs.getString("thumbnail"), item.getThumbnail()) ||
                                    !Objects.equals(rs.getString("author_id"), item.getAuthor_id());

                    if (isDifferent) {

                        updatePs.setString(1, item.getLink());
                        updatePs.setString(2, item.getHeadline());
                        updatePs.setString(3, item.getCategory());
                        updatePs.setString(4, item.getShort_description());
                        updatePs.setString(5, item.getAuthors());
                        updatePs.setString(6, item.getDate());
                        updatePs.setInt(7, item.getViews());
                        updatePs.setString(8, item.getContent());
                        updatePs.setString(9, item.getThumbnail());
                        updatePs.setString(10, item.getAuthor_id());
                        updatePs.setString(11, item.getId());

                        updatePs.executeUpdate();
                        updated++;
                    } else {
                        skipped++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated News: " + updated);
            System.out.println("Skipped News: " + skipped);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class NewsData {

        private String id;
        private String link;
        private String headline;
        private String category;
        private String short_description;
        private String authors;
        private String date;
        private int views;
        private String content;
        private String thumbnail;
        private String author_id;

        public String getId() { return id; }
        public String getLink() { return link; }
        public String getHeadline() { return headline; }
        public String getCategory() { return category; }
        public String getShort_description() { return short_description; }
        public String getAuthors() { return authors; }
        public String getDate() { return date; }
        public int getViews() { return views; }
        public String getContent() { return content; }
        public String getThumbnail() { return thumbnail; }
        public String getAuthor_id() { return author_id; }
    }
}