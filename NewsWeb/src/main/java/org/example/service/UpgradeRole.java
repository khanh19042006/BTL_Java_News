package org.example.service;

import org.example.entity.RoleUpgradeRequest;

import java.util.List;

public interface UpgradeRole {
    public boolean addUser(String userId);              // Thêm user vào danh sách chờ
    public void acpUser(String userId);                 // Đồng ý cho user chuyển role lên journalist
    public List<RoleUpgradeRequest> getListUser();      // Lấy danh sách chờ
    public boolean checkTimeRequest(String userId);     // Kiểm tra xem user này có spam request không
    public boolean checkRole(String userId);            // Kiểm tra tài khoản có role là user mới cho gửi request
}
