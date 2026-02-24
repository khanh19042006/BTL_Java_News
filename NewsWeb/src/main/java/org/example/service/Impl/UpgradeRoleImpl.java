package org.example.service.Impl;

import org.example.dao.RequestUpRoleDAO;
import org.example.dao.UserDAO;
import org.example.entity.RoleUpgradeRequest;
import org.example.service.UpgradeRole;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class UpgradeRoleImpl implements UpgradeRole {
    private final RequestUpRoleDAO requestUpRoleDAO = new RequestUpRoleDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    public boolean addUser(String userId){
        // Chống spam request
        if (!checkTimeRequest(userId)) return false;

        // Kiểm tra role
        if (!checkRole(userId)) return false;

        // Thêm vào record vào db
        String idRequest = UUID.randomUUID().toString();
        boolean check = requestUpRoleDAO.createRoleUpgradeRequest(idRequest, userId);
        if (check){
            // Cập nhật thời gian gửi request cho user
            requestUpRoleDAO.updateRequestAt(userId);
        }
        return check;
    }

    @Override
    public void acpUser(String userId){
        // Chuyển role
        requestUpRoleDAO.upgradeToJournalist(userId);

        // Xóa record trong db
        requestUpRoleDAO.deleteRoleUpgradeRequestByUserId(userId);

        // update lại request_at cho user
        requestUpRoleDAO.updateRequestAt(userId);
    }

    @Override
    public List<RoleUpgradeRequest> getListUser(){
        return requestUpRoleDAO.getAllRoleUpgradeRequests();
    }

    @Override
    public boolean checkTimeRequest(String userId){
        String requestAt = requestUpRoleDAO.getRequestAt(userId);

        // chưa từng gửi => cho phép
        if (requestAt == null) return true;

        return !isWithin30Days(requestAt);
    }

    @Override
    public boolean checkRole(String userId){
        String role = userDAO.getRoleByUserId(userId);
        if (role.equals("user")) return true;
        return false;
    }

    private boolean isWithin30Days(String requestAt) {

        LocalDate requestDate = LocalDate.parse(requestAt);
        LocalDate today = LocalDate.now();

        long daysBetween = ChronoUnit.DAYS.between(requestDate, today);

        return daysBetween < 30;
    }
}
