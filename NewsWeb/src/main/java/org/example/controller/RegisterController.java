package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.dto.UserDTO;
import org.example.service.RegisterService;
import org.example.service.Impl.RegisterServiceImpl;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    // kiểm tra otp
    @FXML private TextField otpField;
    @FXML private HBox otpBox;

    private final RegisterService registerService = new RegisterServiceImpl();


    private String currentUserId; // lưu userId sau khi đăng ký
    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            showError("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Mật khẩu không khớp");
            return;
        }
        if (!registerService.checkUsername(username)) {
            showError("Tên tài khoản đã tồn tại");
            return;
        }
        if (!registerService.checkEmail(email)) {
            showError("Email không hợp lệ");
            return;
        }
        if (!registerService.checkPassword(password)) {
            showError("Mật khẩu phải >=8 ký tự gồm hoa, thường, số, ký tự đặc biệt");
            return;
        }
        // tạo user
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        boolean success = registerService.register(user);

        if (!success) {
            showError("Đăng ký thất bại");
            return;
        }

        // lấy userId để verify OTP
        currentUserId = registerService.getUserIdByUsername(username);

        // gửi OTP
        registerService.sendOtp(email);
        otpBox.setVisible(true);
        otpBox.setManaged(true);
        showError("OTP đã được gửi về email. Vui lòng xác nhận.");
        errorLabel.setStyle("-fx-text-fill: orange;");
    }

    @FXML
    // xác thực otp
    private void handleVerifyOtp() {
        if (currentUserId == null) {
            showError("Vui lòng đăng ký trước.");
            return;
        }
        String otpInput = otpField.getText();
        boolean verified = registerService.verityOtp(currentUserId, otpInput);
        if (verified) {
            showError("Xác thực thành công! Bạn có thể đăng nhập.");
            errorLabel.setStyle("-fx-text-fill: green;");
        } else {
            showError("OTP sai hoặc đã hết hạn.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }


    @FXML
    private void goToLogin() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/Login/login.fxml")
            );
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
