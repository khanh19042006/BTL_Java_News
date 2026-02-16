package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.service.RegisterService;
import org.example.service.Impl.RegisterServiceImpl;

public class VerifyController {

    @FXML private TextField otpField;
    @FXML private Label messageLabel;

    private final RegisterService registerService = new RegisterServiceImpl();

    private String userId;
    private String email;

    public void setData(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    @FXML
    private void handleVerify() {

        String otp = otpField.getText().trim();

        if (otp.isBlank()) {
            showMessage("Vui lòng nhập OTP.", "red");
            return;
        }

        if (userId == null) {
            showMessage("Không tìm thấy người dùng.", "red");
            return;
        }

        boolean verified = registerService.verityOtp(userId, otp);

        if (verified) {
            showMessage("Xác thực thành công!", "green");
            goToLogin();
        } else {
            showMessage("OTP sai hoặc đã hết hạn.", "red");
        }
    }

    @FXML
    private void handleResendOtp() {

        if (email == null || email.isBlank()) {
            showMessage("Không tìm thấy email.", "red");
            return;
        }

        registerService.sendOtp(userId, email);
        showMessage("OTP mới đã được gửi.", "orange");
    }

    private void showMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }

    private void goToLogin() {
        try {
            Stage stage = (Stage) otpField.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/Login/login.fxml")
            );
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Lỗi chuyển trang đăng nhập.", "red");
        }
    }
}