package org.example.service;

public interface LoginService {
    public boolean checkLogin(String username, String password);
    public boolean sendOtp(String email);                           // Gửi otp để xác thực                                            //
    public boolean verityOtp(String userId, String otpInput);       // Xác thực otp
    public boolean changePassword(String userId,
                                  String newPassword1, String newPassword2);    // Đổi  mật khẩu
    public void rememberAuth(String username);                      // Lưu đăng nhập cho lần sau
    public boolean checkTokenTime(String tokenId);                  // Hạn lưu đăng nhập là 7 ngày, kiểm tra xem quá hạn chưa
    public boolean checkAutoLogin(String username);                 // Kiểm tra dăng nhập tự động bằng ghi nhớ đăng nhập
    public String getUserIdByUsername(String username);
}
