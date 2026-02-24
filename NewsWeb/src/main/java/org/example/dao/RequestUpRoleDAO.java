package org.example.dao;

import org.example.entity.RoleUpgradeRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RequestUpRoleDAO {
    public String getRequestAt(String userId) {
        String sql = "SELECT request_at FROM users WHERE id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("request_at"); // có thể trả về null
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean upgradeToJournalist(String userId) {
        String sql = "UPDATE users SET role = ? WHERE id = ? AND role = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "JOURNALIST"); // role mới
            ps.setString(2, userId);       // user cần nâng cấp
            ps.setString(3, "USER");       // chỉ nâng cấp nếu đang là USER

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // true nếu update thành công
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createRoleUpgradeRequest(String requestId, String userId) {
        String sql = "INSERT INTO role_upgrade_requests (id, user_id, created_at) VALUES (?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            String currentDate = java.time.LocalDate.now().toString(); // yyyy-MM-dd

            ps.setString(1, requestId);
            ps.setString(2, userId);
            ps.setString(3, currentDate);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRoleUpgradeRequestByUserId(String userId) {
        String sql = "DELETE FROM role_upgrade_requests WHERE user_id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, userId);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<RoleUpgradeRequest> getAllRoleUpgradeRequests() {
        List<RoleUpgradeRequest> list = new ArrayList<>();
        String sql = "SELECT id, user_id, created_at FROM role_upgrade_requests";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                RoleUpgradeRequest request = new RoleUpgradeRequest();
                request.setId(rs.getString("id"));
                request.setUserId(rs.getString("user_id"));
                request.setCreatedAt(rs.getString("created_at"));

                list.add(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateRequestAt(String userId) {
        String sql = "UPDATE users SET request_at = ? WHERE id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // format yyyy-MM-dd
            String currentDate = java.time.LocalDate.now().toString();

            ps.setString(1, currentDate);
            ps.setString(2, userId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
