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


public class LoginController {

    private final LoginServiceImpl loginService = new LoginServiceImpl();

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
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isSuccess = loginService.checkLogin(username, password);

        if (isSuccess) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Homepage/homepage.fxml")
                );

                Parent root = loader.load();

                // Lấy controller
                HomeController homeController = loader.getController();
                homeController.setUserId(loginService.getUserIdByUsername(username));

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Trang chủ");
                stage.show();

                System.out.println("Đăng nhập thành công");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
//            errorLabel.setText("Tên đăng nhập hoặc mật khẩu sai");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            System.out.println("Đăng nhập thất bại");
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


}
