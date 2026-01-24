package org.example.service.Impl;

import org.example.dao.UserDAO;
import org.example.dto.UserDTO;
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
    public boolean checkUsername(String username){
        boolean isExist = userDAO.checkUsername(username);
        if (isExist) return false;  //username đã tồn tại rồi
        return true;
    }

    @Override
    public boolean checkPassword(String password){
        // Mật khẩu phải dài ít nhất 8 ký tự bao gồm cả chữ hoa, chữ thường, số và ký tự đặc biệt
        if (!PasswordUtils.isValidPassword(password)) return false;
        return true;
    }

    @Override
    public boolean register(UserDTO user){
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
