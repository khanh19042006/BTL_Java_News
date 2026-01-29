package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class ProfileController {

    @FXML
    private ImageView avatarImage;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    public void initialize() {
        usernameLabel.setText("Nguyễn Đình Hoàng");
        emailLabel.setText("nguyendinhhoang.241206@gmail.com");
        roleLabel.setText("ADMIN");
        loadAvatar("/Image/default-thumbnail.jpg");
    }

    private void loadAvatar(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        avatarImage.setImage(image);
    }

    @FXML
    private void onEditAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg"
                )
        );

        File file = fileChooser.showOpenDialog(avatarImage.getScene().getWindow());

        if (file != null) {
            Image newAvatar = new Image(file.toURI().toString());
            avatarImage.setImage(newAvatar);
        }
    }
}
