package org.example.dao;

import org.example.dto.CommentDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommentDAO {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Thêm comment (cha hoặc reply)
    public boolean createComment(String content, String authorId, String newsId, String parentId) {

        String sql = """
                INSERT INTO comment
                (id, content, author_id, news_id, time_up, parent_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String id = UUID.randomUUID().toString();
        String timeUp = LocalDateTime.now().format(formatter);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, content);
            ps.setString(3, authorId);
            ps.setString(4, newsId);
            ps.setString(5, timeUp);

            if (parentId == null) {
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(6, parentId);
            }

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy comment cha theo bài viết
    public List<CommentDTO> getParentComments(String newsId) {

        List<CommentDTO> list = new ArrayList<>();

        String sql = """
            SELECT id, content, author_id, news_id, time_up, parent_id
            FROM comment
            WHERE news_id = ?
              AND parent_id IS NULL
            ORDER BY time_up DESC
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newsId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // Lấy reply theo parentId
    public List<CommentDTO> getReplies(String parentId) {

        List<CommentDTO> list = new ArrayList<>();

        String sql = """
                SELECT * FROM comment
                WHERE parent_id = ?
                ORDER BY time_up ASC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Kiểm tra xem comment hiện tại có reply nào hay không
    public boolean hasReplies(String parentId) {

        String sql = """
            SELECT COUNT(*) 
            FROM comment
            WHERE parent_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xóa comment (chỉ admin mới dùng)
    public boolean deleteCommentByAdmin(String commentId) {

        String sql = "DELETE FROM comment WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, commentId);
            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Chỉ chủ comment mới có thể xóa
    public boolean deleteCommentByUser(String commentId, String userId) {

        String sql = """
            DELETE FROM comment
            WHERE id = ?
              AND author_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, commentId);
            ps.setString(2, userId);

            int affectedRows = ps.executeUpdate();

            // nếu affectedRows = 1 → xóa thành công
            return affectedRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Kiểm tra comment có thuộc về user không
    public boolean isOwner(String commentId, String userId) {

        String sql = """
            SELECT COUNT(*) 
            FROM comment
            WHERE id = ?
            AND author_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, commentId);
            ps.setString(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // Map ResultSet → DTO
    private CommentDTO mapResultSet(ResultSet rs) throws SQLException {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(rs.getString("id"));
        commentDTO.setContent(rs.getString("content"));
        commentDTO.setAuthorId(rs.getString("author_id"));
        commentDTO.setNewsId(rs.getString("news_id"));
        commentDTO.setTimeUp(rs.getString("parent_id"));

        return commentDTO;
    }
}
