package org.example.dao;

import org.example.entity.News;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    // Lấy headline + short_description
    public List<News> getAllNewsSummary(int limit) {
        List<News> newsList = new ArrayList<>();

        String sql = """
        SELECT headline, short_description
        FROM news
        ORDER BY date DESC
        LIMIT ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    News news = new News();
                    news.setHeadline(rs.getString("headline"));
                    news.setShort_description(rs.getString("short_description"));
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
