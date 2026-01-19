package org.example.dao;

import org.example.entity.News;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    // 10 bài báo mới nhất
    public List<News> getNewNews(int limit) {
        List<News> newsList = new ArrayList<>();

        String sql = """
        SELECT headline
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
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    //10 bài báo có view cao nhất
    public List<News> getHotNews(int limit) {
        List<News> newsList = new ArrayList<>();

        String sql = """
        SELECT headline
        FROM news
        ORDER BY view DESC, date DESC
        LIMIT ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    News news = new News();
                    news.setHeadline(rs.getString("headline"));
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
