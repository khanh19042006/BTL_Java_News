package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.dto.NewsDTO;
import org.example.dto.UserDTO;
import org.example.service.ProfileService;
import org.example.service.Impl.ProfileServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import java.util.List;

public class ProfileController {

    @FXML private ImageView avatarImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private ListView<NewsDTO> userPostsList;

    private final ProfileService profileService = new ProfileServiceImpl();

    private static final double AVATAR_SIZE = 80;
    private static final String AVATAR_DIR = "user-data/avatars/";
    private static final String DEFAULT_AVATAR = "/Image/default-thumbnail.jpg";

    // hardcode admin
    private final String currentUserId =
            "c505cc32-1ea9-47a2-b936-327aaf483db";

    @FXML
    public void initialize() {
        setupListView();
        loadUserInfo();
        loadAvatar();
        loadUserNews();
    }

    private void loadUserInfo() {
        UserDTO user = profileService.getUserById(currentUserId);

        if (user == null) {
            usernameLabel.setText("Unknown");
            emailLabel.setText("");
            roleLabel.setText("");
            return;
        }

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(user.getRole());
    }

    private void loadUserNews() {
        List<NewsDTO> news =
                profileService.getNewsByUserId(currentUserId);

        if (news == null || news.isEmpty()) {
            userPostsList.setPlaceholder(
                    new Label("📰 Chưa có bài viết")
            );
            return;
        }

        userPostsList.setItems(
                FXCollections.observableArrayList(news)
        );
    }

    private void setupListView() {
        userPostsList.setCellFactory(list -> new ListCell<>() {

            private final ImageView imageView = new ImageView();
            private final Label title = new Label();
            private final Label description = new Label();
            private final Label date = new Label();

            private final VBox textBox = new VBox(6);
            private final HBox root = new HBox(12);

            {
                imageView.setFitWidth(90);
                imageView.setFitHeight(65);
                imageView.setPreserveRatio(true);

                title.setWrapText(true);
                title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                description.setWrapText(true);
                description.setStyle("-fx-font-size: 12px;");

                date.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");

                textBox.getChildren().addAll(title, description, date);
                textBox.setPrefWidth(260);

                root.getChildren().addAll(imageView, textBox);
                root.setStyle("""
                        -fx-padding: 10;
                        -fx-background-color: white;
                        -fx-background-radius: 8;
                        -fx-border-radius: 8;
                        -fx-border-color: #E0E0E0;
                        """);
            }

            @Override
            protected void updateItem(NewsDTO news, boolean empty) {
                super.updateItem(news, empty);

                if (empty || news == null) {
                    setGraphic(null);
                    return;
                }

                title.setText(news.getHeadline());
                description.setText(news.getShort_description());
                date.setText("🕒 " + news.getDate());

                imageView.setImage(loadImage(news.getThumbnail()));

                setGraphic(root);
            }
        });
    }

    private Image loadImage(String path) {
        try {
            if (path == null || path.isBlank())
                throw new Exception();

            return new Image(
                    getClass().getResource(path).toExternalForm()
            );
        } catch (Exception e) {
            return new Image(
                    getClass()
                            .getResource(DEFAULT_AVATAR)
                            .toExternalForm()
            );
        }
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

        File file = chooser.showOpenDialog(
                avatarImage.getScene().getWindow()
        );
        if (file == null) return;

        try {
            Files.createDirectories(Path.of(AVATAR_DIR));

            Path target = Path.of(
                    AVATAR_DIR + "avatar_" + currentUserId + ".jpg"
            );

            Files.copy(
                    file.toPath(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            setAvatar(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAvatar() {
        Path avatarPath = Path.of(
                AVATAR_DIR + "avatar_" + currentUserId + ".jpg"
        );

        if (Files.exists(avatarPath)) {
            setAvatar(avatarPath);
        } else {
            avatarImage.setImage(
                    new Image(
                            getClass()
                                    .getResource(DEFAULT_AVATAR)
                                    .toExternalForm(),
                            AVATAR_SIZE,
                            AVATAR_SIZE,
                            true,
                            true
                    )
            );
        }
    }

    private void setAvatar(Path path) {
        avatarImage.setImage(
                new Image(
                        path.toUri().toString(),
                        AVATAR_SIZE,
                        AVATAR_SIZE,
                        true,
                        true
                )
        );
    }
}
