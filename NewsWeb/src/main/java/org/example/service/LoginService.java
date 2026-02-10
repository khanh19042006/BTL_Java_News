package org.example.service;

public interface LoginService {
    public boolean checkLogin(String username, String password);
    public boolean sendOtp(String email);                           // Gửi otp để xác thực                                            //
    public boolean verityOtp(String userId, String otpInput);       // Xác thực otp
    public boolean changePassword(String userId,
                                  String newPassword1, String newPassword2);    // Đổi  mật khẩu
}
