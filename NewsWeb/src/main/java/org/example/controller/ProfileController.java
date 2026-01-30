package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ProfileController {

    @FXML private ImageView avatarImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;

    private static final double AVATAR_SIZE = 80;

    private static final String AVATAR_DIR = "user-data/avatars/";

    @FXML
    public void initialize() {
        usernameLabel.setText("Nguyễn Đình Hoàng");
        emailLabel.setText("nguyendinhhoang.241206@gmail.com");
        roleLabel.setText("ADMIN");

        loadAvatarFromLocal();
    }

    @FXML
    private void onEditAvatar() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chọn ảnh đại diện");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg"
                )
        );

        File selectedFile = chooser.showOpenDialog(
                avatarImage.getScene().getWindow()
        );

        if (selectedFile == null) return;

        try {
            Files.createDirectories(Path.of(AVATAR_DIR));

            String avatarName = "user_avatar.jpg";
            Path target = Path.of(AVATAR_DIR + avatarName);

            Files.copy(
                    selectedFile.toPath(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            Image avatar = new Image(
                    target.toUri().toString(),
                    AVATAR_SIZE,
                    AVATAR_SIZE,
                    true,
                    true
            );
            avatarImage.setImage(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAvatarFromLocal() {
        Path avatarPath = Path.of(AVATAR_DIR + "user_avatar.jpg");

        if (Files.exists(avatarPath)) {
            avatarImage.setImage(
                    new Image(
                            avatarPath.toUri().toString(),
                            AVATAR_SIZE,
                            AVATAR_SIZE,
                            true,
                            true
                    )
            );
        } else {
            avatarImage.setImage(
                    new Image(
                            getClass()
                                    .getResource("/Image/default-thumbnail.jpg")
                                    .toExternalForm(),
                            AVATAR_SIZE,
                            AVATAR_SIZE,
                            true,
                            true
                    )
            );
        }
    }
}
