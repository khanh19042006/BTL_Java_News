package org.example.service.Impl;

import org.example.dao.UserDAO;
import org.example.entity.User;
import org.example.service.RegisterService;
import org.example.utils.EmailUtils;
import org.example.utils.PasswordUtils;

import java.util.regex.Pattern;

public class RegisterServiceImpl implements RegisterService {

    private final UserDAO userDAO;

    public RegisterServiceImpl(){
        this.userDAO = new UserDAO();
    }

    @Override
    public boolean checkEmail(String email){
        return EmailUtils.isValid(email);
    }

    @Override
    public boolean register(User user){
        boolean isExist = userDAO.checkUsername(user.getUsername());
        if (isExist) return false;
        //Kiểm tra email có định dạng phù hợp không
        if (!checkEmail(user.getEmail())) return false;

        //Kiểm tra độ mạnh của mật khẩu
        if (!PasswordUtils.isValidPassword(user.getPassword()))  return false;

        //Khởi tạo user trong DB
        user.setPassword(PasswordUtils.hash(user.getPassword()));
        userDAO.createUser(user);
        return true;
    }
}
