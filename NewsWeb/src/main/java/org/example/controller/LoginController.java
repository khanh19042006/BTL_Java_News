package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.service.Impl.LoginServiceImpl;

import java.io.IOException;
import javafx.scene.control.Label;
import org.example.service.LoginService;
import javafx.scene.control.CheckBox;
import java.util.prefs.Preferences;


public class LoginController {
    private final LoginService loginService = new LoginServiceImpl();

    //nhớ ta khoản
    @FXML
    private CheckBox rememberCheckBox;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button togglePasswordBtn;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        String username = usernameField.getText();
        if (loginService.checkAutoLogin(username)) {
            goToHome(username);
        }
    }

    @FXML
    private void handleLogin() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        String username = usernameField.getText();
        String password = passwordField.isVisible() ? passwordField.getText() : passwordTextField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Vui lòng nhập đầy đủ thông tin!");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }
        boolean isSuccess = loginService.checkLogin(username, password);

        if (isSuccess) {
            // lưu tài khoản
            if (rememberCheckBox.isSelected()) {
                loginService.rememberAuth(username);
                goToHome(username);
            }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }

    // Hiện mật khẩu khi bấm vào con mắt
    @FXML
    private void togglePasswordVisibility() {

        if (passwordField.isVisible()) {
            // Hiện mật khẩu
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);

            togglePasswordBtn.setText("🙈");
        } else {
            // Ẩn mật khẩu
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);

            togglePasswordBtn.setText("👁");
        }
    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Register/register.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng ký");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ForgotPassword/forgot-password.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quên mật khẩu");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void goToHome(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Homepage/homepage.fxml")
            );

            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setUserId(
                    loginService.getUserIdByUsername(username)
            );

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Trang chủ");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
