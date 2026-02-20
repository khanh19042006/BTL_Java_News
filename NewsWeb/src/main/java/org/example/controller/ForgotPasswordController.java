package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.service.LoginService;
import org.example.service.Impl.LoginServiceImpl;

public class ForgotPasswordController {

    @FXML private Button sendOtpBtn;
    @FXML private Button resendBtn;
    @FXML private Button confirmOtpBtn;

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField otpField;
    @FXML private PasswordField newPasswordField;

    @FXML private Label messageLabel;

    private final LoginService loginService = new LoginServiceImpl();

    private String currentUserId = null;
    private boolean otpVerified = false;

    @FXML
    private void handleSendOtp() {

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ Username và Email!", true);
            return;
        }

        // check username + email tại service
        if (!loginService.checkUsernameAndEmail(username, email)) {
            showMessage("Username và Email không khớp hoặc không tồn tại!", true);
            return;
        }

        currentUserId = loginService.getUserIdByUsername(username);

        if (currentUserId == null) {
            showMessage("Tài khoản không tồn tại!", true);
            return;
        }

        if (!loginService.sendOtp(email)) {
            showMessage("Gửi OTP thất bại!", true);
            return;
        }

        showMessage("OTP đã gửi về email!", false);

        sendOtpBtn.setVisible(false);
        sendOtpBtn.setManaged(false);

        resendBtn.setVisible(true);
        resendBtn.setManaged(true);

        confirmOtpBtn.setVisible(true);
        confirmOtpBtn.setManaged(true);
    }

    @FXML
    private void handleResendOtp() {

        if (currentUserId == null) {
            showMessage("Vui lòng nhập lại thông tin!", true);
            return;
        }

        String email = emailField.getText().trim();

        if (!loginService.sendOtp(email)) {
            showMessage("Gửi lại OTP thất bại!", true);
            return;
        }

        showMessage("Đã gửi lại OTP!", false);
    }

    @FXML
    private void handleConfirmOtp() {

        if (currentUserId == null) {
            showMessage("Vui lòng gửi OTP trước!", true);
            return;
        }

        String otp = otpField.getText().trim();

        if (otp.isEmpty()) {
            showMessage("Vui lòng nhập OTP!", true);
            return;
        }

        if (!loginService.verityOtp(currentUserId, otp)) {
            showMessage("OTP sai hoặc hết hạn!", true);
            return;
        }

        otpVerified = true;
        showMessage("Xác thực OTP thành công!", false);
    }

    @FXML
    private void handleResetPassword() {

        if (!otpVerified) {
            showMessage("Vui lòng xác nhận OTP trước!", true);
            return;
        }

        String newPass = newPasswordField.getText().trim();

        if (newPass.isEmpty()) {
            showMessage("Vui lòng nhập mật khẩu mới!", true);
            return;
        }

        if (!loginService.changePassword(currentUserId, newPass, newPass)) {
            showMessage("Đổi mật khẩu thất bại!", true);
            return;
        }

        showMessage("Đổi mật khẩu thành công!", false);

        // reset trạng thái sau khi đổi xong
        otpVerified = false;
        currentUserId = null;
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/Login/login.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message, boolean isError) {

        messageLabel.setText(message);
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);

        if (isError) {
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
        }
    }
}