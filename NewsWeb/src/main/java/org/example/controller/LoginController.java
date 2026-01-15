package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.service.Impl.LoginServiceImpl;

import java.awt.*;

public class LoginController {

    private final LoginServiceImpl loginService = new LoginServiceImpl();

    @FXML
    private TextField name_input;

    @FXML
    private PasswordField pass_input;

    @FXML
    private void handleLogin(){
        String username = name_input.getText();
        String password = pass_input.getText();

        boolean isSuccess = loginService.checkLogin(username, password);
        if (isSuccess){
            System.out.println("Đăng nhập thành công");
        } else{
            System.out.println("Đăng nhập thất bại");
        }
    }
}
