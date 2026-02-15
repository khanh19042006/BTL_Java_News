package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.dto.UserDTO;
import org.example.service.RegisterService;
import org.example.service.Impl.RegisterServiceImpl;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private final RegisterService registerService = new RegisterServiceImpl();

    @FXML
    private void handleRegister() {

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // kểm tra đầy đủ thông tin
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showMessage("Vui lòng nhập đầy đủ thông tin.", "red");
            return;
        }

        // kiểm tra password trùng
        if (!password.equals(confirmPassword)) {
            showMessage("Mật khẩu không khớp.", "red");
            return;
        }

        // check username
        if (!registerService.checkUsername(username)) {
            showMessage("Username đã tồn tại.", "red");
            return;
        }

        // check email format
        if (!registerService.checkEmail(email)) {
            showMessage("Email không hợp lệ.", "red");
            return;
        }

        // check password mạnh
        if (!registerService.checkPassword(password)) {
            showMessage("Mật khẩu chưa đủ mạnh.", "red");
            return;
        }

        // tạo DTO
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        boolean success = registerService.register(user);

        if (!success) {
            showMessage("Đăng ký thất bại.", "red");
            return;
        }

        // lấy userId lại từ DB
        String userId = registerService.getUserIdByUsername(username);

        if (userId == null) {
            showMessage("Tạo tài khoản thất bại. Vui lòng thử lại.", "red");
            return;
        }

        //  gửi OTP
        registerService.sendOtp(userId, email);

        goToVerify(userId, email);
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
            showMessage("Lỗi chuyển trang.", "red");
        }
    }

    private void goToVerify(String userId, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Register/verify.fxml")
            );

            Parent root = loader.load();

            VerifyController controller = loader.getController();
            controller.setData(userId, email);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Lỗi chuyển trang xác thực.", "red");
        }
    }

    private void showMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }
}