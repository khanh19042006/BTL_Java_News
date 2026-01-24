package org.example.service.Impl;

import org.example.dao.UserDAO;
import org.example.service.LoginService;
import org.example.utils.PasswordUtils;

public class LoginServiceImpl implements LoginService {

    private final UserDAO userDAO;

    public LoginServiceImpl() {
        this.userDAO = new UserDAO();
    }

    @Override
    public boolean checkLogin(String username, String password){

        String passwordHash = userDAO.getPasswordByUsername(username);
        if (passwordHash == null) return false;

        return PasswordUtils.check(password, passwordHash);
    }
}
