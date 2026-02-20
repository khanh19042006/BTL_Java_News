package org.example.service;

import org.example.dto.UserDTO;

public interface RegisterService {
    public boolean register(UserDTO user);
    public boolean checkEmail(String email);
    public boolean checkUsername(String username);
    public boolean checkPassword (String password);
    public String getUserIdByUsername(String username);
    public String createOtp(String userId);
    public void sendOtp(String userId, String toEmail);
    public boolean verityOtp(String userId, String otpInput);
}
