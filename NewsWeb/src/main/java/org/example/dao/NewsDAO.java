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
        SELECT id,
                headline,
               category,
               short_description,
               content,
               thumbnail,
               authors,
               date,
               views,
                author_id
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
                    news.setId(rs.getString("id"));
                    news.setHeadline(rs.getString("headline"));
                    news.setCategory(rs.getString("category"));
                    news.setShort_description(rs.getString("short_description"));
                    news.setContent(rs.getString("content"));
                    news.setThumbnail(rs.getString("thumbnail"));
                    news.setAuthors(rs.getString("authors"));
                    news.setDate(rs.getString("date"));
                    news.setViews(rs.getInt("views"));
                    news.setAuthorId(rs.getString("author_id"));
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
        SELECT id,
                headline,
               category,
               short_description,
               content,
               thumbnail,
               authors,
               date,
               views,
                author_id
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
                    news.setId(rs.getString("id"));
                    news.setHeadline(rs.getString("headline"));
                    news.setCategory(rs.getString("category"));
                    news.setShort_description(rs.getString("short_description"));
                    news.setContent(rs.getString("content"));
                    news.setThumbnail(rs.getString("thumbnail"));
                    news.setAuthors(rs.getString("authors"));
                    news.setDate(rs.getString("date"));
                    news.setViews(rs.getInt("views"));
                    news.setAuthorId(rs.getString("author_id"));

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
            id,
            headline,
            category,
            short_description,
            authors,
            date,
            views,
            content,
            thumbnail,
            author_id,
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

                    dto.setId(rs.getString("id"));
                    dto.setHeadline(rs.getString("headline"));
                    dto.setCategory(rs.getString("category"));
                    dto.setShort_description(rs.getString("short_description"));
                    dto.setAuthors(rs.getString("authors"));
                    dto.setDate(rs.getString("date"));
                    dto.setViews(rs.getInt("views"));
                    dto.setContent(rs.getString("content"));
                    dto.setThumbnail(rs.getString("thumbnail"));
                    dto.setAuthorId(rs.getString("author_id"));

                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<NewsDTO> getNewsByCategory(String categoryName, int limit) {
        List<NewsDTO> list = new ArrayList<>();
        String categoryCode = getCategoryCodeByName(categoryName);

        String sql = """
        SELECT
            id,
            headline,
            category,
            short_description,
            authors,
            date,
            views,
            content,
            thumbnail,
            author_id
        FROM news
        WHERE category = ?
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
                    dto.setId(rs.getString("id"));
                    dto.setHeadline(rs.getString("headline"));
                    dto.setCategory(rs.getString("category"));
                    dto.setShort_description(rs.getString("short_description"));
                    dto.setAuthors(rs.getString("authors"));
                    dto.setDate(rs.getString("date"));
                    dto.setViews(rs.getInt("views"));
                    dto.setContent(rs.getString("content"));
                    dto.setThumbnail(rs.getString("thumbnail"));
                    dto.setAuthorId(rs.getString("author_id"));
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private String getCategoryCodeByName(String categoryName) {
        String sql = "SELECT code FROM category WHERE name = ?";
        String code = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return code;
    }

    public List<NewsDTO> getNewsByAuthorId(String authorId) {

        List<NewsDTO> list = new ArrayList<>();

        String sql = """
        SELECT id, headline, category, short_description,
               authors, date, views, content,
               thumbnail, author_id
        FROM news
        WHERE author_id = ?
        ORDER BY date DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, authorId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NewsDTO dto = new NewsDTO();

                dto.setId(rs.getString("id"));
                dto.setHeadline(rs.getString("headline"));
                dto.setCategory(rs.getString("category"));
                dto.setShort_description(rs.getString("short_description"));
                dto.setAuthors(rs.getString("authors"));
                dto.setDate(rs.getString("date"));
                dto.setViews(rs.getInt("views"));
                dto.setContent(rs.getString("content"));
                dto.setThumbnail(rs.getString("thumbnail"));
                dto.setAuthorId(rs.getString("author_id"));

                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public NewsDTO getNewsById(String id) {

        String sql = """
        SELECT
            id,
            headline,
            category,
            short_description,
            authors,
            date,
            views,
            content,
            thumbnail,
            author_id
        FROM news
        WHERE id = ?
        LIMIT 1
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NewsDTO dto = new NewsDTO();

                    dto.setId(rs.getString("id"));
                    dto.setHeadline(rs.getString("headline"));
                    dto.setCategory(rs.getString("category"));
                    dto.setShort_description(rs.getString("short_description"));
                    dto.setAuthors(rs.getString("authors"));
                    dto.setDate(rs.getString("date"));
                    dto.setViews(rs.getInt("views"));
                    dto.setContent(rs.getString("content"));
                    dto.setThumbnail(rs.getString("thumbnail"));
                    dto.setAuthorId(rs.getString("author_id"));

                    return dto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateNews(NewsDTO newsDTO) {

        String sql = """
        UPDATE news
        SET headline = ?,
            category = ?,
            short_description = ?,
            content = ?,
            thumbnail = ?
        WHERE id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newsDTO.getHeadline());
            ps.setString(2, newsDTO.getCategory());
            ps.setString(3, newsDTO.getShort_description());
            ps.setString(4, newsDTO.getContent());
            ps.setString(5, newsDTO.getThumbnail());
            ps.setString(6, newsDTO.getId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
