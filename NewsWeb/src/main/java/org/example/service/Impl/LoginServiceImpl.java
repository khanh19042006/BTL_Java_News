package org.example.service.Impl;

import org.example.dao.UserDAO;
import org.example.service.LoginService;

public class LoginServiceImpl implements LoginService {

    private final UserDAO userDAO;

    public LoginServiceImpl() {
        this.userDAO = new UserDAO();
    }

    @Override
    public boolean checkLogin(String username, String password){
        return userDAO.checkUser(username, password);
    }
}
