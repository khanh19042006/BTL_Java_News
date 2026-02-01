package org.example.dao;

import org.example.dto.NewsDTO;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {

    // Lưu bài viết đã đọc vào lịch sử đọc
    public boolean saveNews(String userId, String newsId) {

        String sql = """
        INSERT INTO history (id, user_id, news_id, read_at)
        VALUES (?, ?, ?, ?)
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Tạo UUID cho id
            String id = java.util.UUID.randomUUID().toString();

            // Lấy ngày hiện tại (yyyy-MM-dd)
            String currentDate = java.time.LocalDate.now().toString();

            ps.setString(1, id);
            ps.setString(2, userId);
            ps.setString(3, newsId);
            ps.setString(4, currentDate);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy lịch sử đọc báo của người dùng
    public List<NewsDTO> getHistoryNews(String userId, int limit) {

        List<NewsDTO> list = new ArrayList<>();

        String sql = """
        SELECT n.id,
               n.headline,
               n.category,
               n.short_description,
               n.authors,
               n.date,
               n.views,
               n.content,
               n.thumbnail,
               n.author_id
        FROM history h
        JOIN news n ON h.news_id = n.id
        WHERE h.user_id = ?
        ORDER BY h.read_at DESC
        LIMIT ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
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

}
