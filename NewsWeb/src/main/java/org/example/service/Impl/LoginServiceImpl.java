package org.example.service.Impl;

import org.example.dao.AuthDAO;
import org.example.service.LoginService;
import org.example.utils.PasswordUtils;

public class LoginServiceImpl implements LoginService {

    private final AuthDAO authDAO;

    public LoginServiceImpl() {
        this.authDAO = new AuthDAO();
    }

    @Override
    public boolean checkLogin(String username, String password){

        String passwordHash = authDAO.getPasswordByUsername(username);
        boolean isVerity = authDAO.isCheckVerity(username);
        if (passwordHash == null) return false;
        // Kiểm tra xác thực
        if (!isVerity) return false;

        return PasswordUtils.check(password, passwordHash);
    }
}
