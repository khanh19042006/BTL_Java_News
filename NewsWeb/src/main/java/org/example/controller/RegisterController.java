package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private final RegisterService registerService = new RegisterServiceImpl();

    @FXML
    private void handleRegister() {

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Kiểm tra rỗng
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            showError("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        // Kiểm tra mật khẩu khớp
        if (!password.equals(confirmPassword)) {
            showError("Mật khẩu không khớp");
            return;
        }

        // Kiểm tra username
        if (!registerService.checkUsername(username)) {
            showError("Tên tài khoản đã tồn tại");
            return;
        }

        // Kiểm tra email
        if (!registerService.checkEmail(email)) {
            showError("Email không hợp lệ");
            return;
        }

        // Kiểm tra password mạnh
        if (!registerService.checkPassword(password)) {
            showError("Mật khẩu phải >=8 ký tự gồm hoa, thường, số, ký tự đặc biệt");
            return;
        }

        // Tạo user DTO
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        boolean success = registerService.register(user);

        if (!success) {
            showError("Đăng ký thất bại");
            return;
        }

        // Gửi OTP
        registerService.sendOtp(email);

        showError("Đăng ký thành công! Vui lòng kiểm tra email để xác thực.");
        errorLabel.setStyle("-fx-text-fill: green;");
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
