package org.example.service.Impl;

import org.example.dao.UserDAO;
import org.example.service.UserSevice;

public class UserServiceImpl implements UserSevice {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public boolean isAdmin(String userId){
        return userDAO.isAdmin(userId);
    }

    @Override
    public boolean isJournalist(String userId){
        return userDAO.isJournalist(userId);
    }
}
