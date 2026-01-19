package org.example.dao;

import org.example.entity.News;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    // Lấy 10 bài báo mới nhất
    // Lấy các bài báo mới nhất
    public List<News> getNewNews(int limit) {
        List<News> newsList = new ArrayList<>();

        String sql = """
        SELECT headline,
               category,
               short_description,
               content,
               thumbnail,
               authors,
               date,
               view
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
                    news.setCategory(rs.getString("category"));
                    news.setShort_description(rs.getString("short_description"));
                    news.setContent(rs.getString("content"));      // NEW
                    news.setThumbnail(rs.getString("thumbnail"));  // NEW
                    news.setAuthors(rs.getString("authors"));
                    news.setDate(rs.getString("date"));
                    news.setView(rs.getInt("view"));

                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }


    // Lấy 10 bài báo có view cao nhất
    public List<News> getHotNews(int limit) {
        List<News> newsList = new ArrayList<>();

        String sql = """
        SELECT headline,
               category,
               short_description,
               content,
               thumbnail,
               authors,
               date,
               view
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
                    news.setCategory(rs.getString("category"));
                    news.setShort_description(rs.getString("short_description"));
                    news.setContent(rs.getString("content"));      // NEW
                    news.setThumbnail(rs.getString("thumbnail"));  // NEW
                    news.setAuthors(rs.getString("authors"));
                    news.setDate(rs.getString("date"));
                    news.setView(rs.getInt("view"));

                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }


}
