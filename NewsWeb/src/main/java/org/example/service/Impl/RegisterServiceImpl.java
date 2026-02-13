package org.example.service.Impl;

import com.sun.javafx.embed.EmbeddedSceneInterface;
import org.example.dao.AuthDAO;
import org.example.dto.UserDTO;
import org.example.service.RegisterService;
import org.example.utils.EmailUtils;
import org.example.utils.PasswordUtils;

public class RegisterServiceImpl implements RegisterService {

    private final AuthDAO authDAO;

    public RegisterServiceImpl(){
        this.authDAO = new AuthDAO();
    }

    @Override
    public boolean checkEmail(String email){
        return EmailUtils.isValid(email);
    }

    @Override
    public boolean checkUsername(String username){
        boolean isExist = authDAO.checkUsername(username);
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
        boolean isExist = authDAO.checkUsername(user.getUsername());
        if (isExist) return false;
        //Kiểm tra email có định dạng phù hợp không
        if (!this.checkEmail(user.getEmail())) return false;

        //Kiểm tra độ mạnh của mật khẩu
        if (!this.checkPassword(user.getPassword()))  return false;

        //Khởi tạo user trong DB
        user.setPassword(PasswordUtils.hash(user.getPassword()));
        String userId = authDAO.createUser(user);
        return true;
    }

    @Override
    public String getUserIdByUsername(String username){
        return authDAO.getUserIdByUsername(username);
    }

    @Override
    public String createOtp(String userId){
        return authDAO.createOtp(userId);
    }

    @Override
    public void sendOtp(String toEmail){
        String userId = authDAO.getUserIdByEmail(toEmail);
        String otp = this.createOtp(userId);
        EmailUtils.sendOTP(toEmail, otp);
        return;
    }

    @Override
    public boolean verityOtp(String userId, String otpInput){
        // Kiểm tra otp nhập đúng chưa, còn hạn không
        if (authDAO.verifyOtp(userId, otpInput)){
            // Chuyển trạng thái cho tài khoản
            if (!authDAO.updateUserVerified(userId)) return false;
            // Xóa token otp
            authDAO.deleteToken(userId);
            return true;
        }
        return false;
    }
}
