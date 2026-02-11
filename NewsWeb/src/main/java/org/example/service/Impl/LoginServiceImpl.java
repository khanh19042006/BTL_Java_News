package org.example.service.Impl;

import org.example.dao.AuthDAO;
import org.example.service.LoginService;
import org.example.utils.EmailUtils;
import org.example.utils.PasswordUtils;
import org.example.utils.RememberToken;

import java.util.UUID;

public class LoginServiceImpl implements LoginService {

    private final AuthDAO authDAO = new AuthDAO();

    @Override
    public boolean checkLogin(String username, String password){
        String passwordHash = authDAO.getPasswordByUsername(username);
        String userId = authDAO.getUserIdByUsername(username);
        boolean isVerity = authDAO.isCheckVerity(userId);
        if (passwordHash == null) return false;
        // Kiểm tra xác thực
        if (!isVerity) return false;

        return PasswordUtils.check(password, passwordHash);
    }

    @Override
    public boolean sendOtp(String email){
        String userId = authDAO.getUserIdByEmail(email);
        // Email không tồn tại
        if (userId == null) return false;

        // Tạo otp
        String otp = authDAO.createOtp(userId);

        // Gửi otp
        if (!EmailUtils.sendOTP(email, otp)) return false;
        return true;
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

    @Override
    public boolean changePassword(String userId, String newPassword1, String newPassword2){
        // Tài khoản phải được xác thực thì mới cho phép đổi
        boolean isVerity = authDAO.isCheckVerity(userId);
        if (!isVerity) return false;

        // Mật khẩu mới và xác thực lại mật khẩu phải giống nhau
        if (!newPassword1.equalsIgnoreCase(newPassword2)) return false;

        // Kiểm tra độ mạnh yếu của mật khẩu
        if (!PasswordUtils.isValidPassword(newPassword1)) return false;

        // Đổi mật khẩu trong db
        if (!authDAO.changePassword(userId, newPassword1)) return false;
        return true;
    }

    @Override
    public void rememberAuth(String username){
        String userId = authDAO.getUserIdByUsername(username);
        // Kiểm tra username tồn tại
        if (userId == null) return;

        // Lưu vào db
        String tokenId = UUID.randomUUID().toString();
        if (!authDAO.saveRememberToken(userId, tokenId)) return;

        // Lưu vào file local
        RememberToken.saveTokenToLocal(tokenId);
        return;
    }

    @Override
    public boolean checkTokenTime(String tokenId){
        boolean checkTime = authDAO.isRememberTokenExpired(tokenId);
        // Đã quá hạn
        if (checkTime) {
            authDAO.deleteRememberToken(tokenId);
            return false;
        }
        return true;
    }

    @Override
    public boolean checkAutoLogin(String username){
        String userId = authDAO.getUserIdByUsername(username);
        String tokenId = RememberToken.getTokenFromLocal();
        String userIdLocal = authDAO.getUserIdByRememberToken(tokenId);

        // Đã quá hạn tự động đăng nhập
        if (!checkTokenTime(tokenId)) return false;

        // Kiểm tra tokenId local và tokenId trong db trùng nhauu
        if (userIdLocal.equalsIgnoreCase(userId)) return true;
        return false;
    }
}
