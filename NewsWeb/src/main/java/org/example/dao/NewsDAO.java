package org.example.dao;

import org.example.dto.NewsDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    // Lấy 10 bài báo mới nhất
    // Lấy các bài báo mới nhất
    public List<NewsDTO> getNewNews(int limit) {
        List<NewsDTO> newsList = new ArrayList<>();

        String sql = """
        SELECT headline,
               category,
               short_description,
               content,
               thumbnail,
               authors,
               date,
               views
        FROM news
        ORDER BY date DESC
        LIMIT ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewsDTO news = new NewsDTO();
                    news.setHeadline(rs.getString("headline"));
                    news.setCategory(rs.getString("category"));
                    news.setShort_description(rs.getString("short_description"));
                    news.setContent(rs.getString("content"));
                    news.setThumbnail(rs.getString("thumbnail"));
                    news.setAuthors(rs.getString("authors"));
                    news.setDate(rs.getString("date"));
                    news.setViews(rs.getInt("views"));

                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }


    // Lấy 10 bài báo có view cao nhất
    public List<NewsDTO> getHotNews(int limit) {
        List<NewsDTO> newsList = new ArrayList<>();

        String sql = """
        SELECT headline,
               category,
               short_description,
               content,
               thumbnail,
               authors,
               date,
               views
        FROM news
        ORDER BY views DESC, date DESC
        LIMIT ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewsDTO news = new NewsDTO();
                    news.setHeadline(rs.getString("headline"));
                    news.setCategory(rs.getString("category"));
                    news.setShort_description(rs.getString("short_description"));
                    news.setContent(rs.getString("content"));
                    news.setThumbnail(rs.getString("thumbnail"));
                    news.setAuthors(rs.getString("authors"));
                    news.setDate(rs.getString("date"));
                    news.setViews(rs.getInt("views"));

                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    public List<NewsDTO> searchNews(String keyword, int limit) {
        List<NewsDTO> list = new ArrayList<>();

        String sql = """
        SELECT
            headline,
            category_code,
            short_description,
            authors,
            date,
            views,
            content,
            thumbnail,
            (
              MATCH(headline) AGAINST (?) * 3 +
              MATCH(short_description) AGAINST (?) * 2 +
              MATCH(content) AGAINST (?) * 1
            ) AS score
        FROM news
        WHERE
            MATCH(headline, short_description, content) AGAINST (?)
        ORDER BY score DESC
        LIMIT (?)""";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ps.setInt(5, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewsDTO dto = new NewsDTO();

                    dto.setHeadline(rs.getString("headline"));
                    dto.setCategory(rs.getString("category_code"));
                    dto.setShort_description(rs.getString("short_description"));
                    dto.setAuthors(rs.getString("authors"));
                    dto.setDate(rs.getString("date"));
                    dto.setViews(rs.getInt("views"));
                    dto.setContent(rs.getString("content"));
                    dto.setThumbnail(rs.getString("thumbnail"));

                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<NewsDTO> getNewsByCategory(String categoryCode, int limit) {
        List<NewsDTO> list = new ArrayList<>();

        String sql = """
        SELECT
            headline,
            category_code,
            short_description,
            authors,
            date,
            views,
            content,
            thumbnail
        FROM news
        WHERE category_code = ?
        ORDER BY date DESC
        LIMIT ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryCode);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewsDTO dto = new NewsDTO();
                    dto.setHeadline(rs.getString("headline"));
                    dto.setCategory(rs.getString("category_code"));
                    dto.setShort_description(rs.getString("short_description"));
                    dto.setAuthors(rs.getString("authors"));
                    dto.setDate(rs.getString("date"));
                    dto.setViews(rs.getInt("views"));
                    dto.setContent(rs.getString("content"));
                    dto.setThumbnail(rs.getString("thumbnail"));
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
