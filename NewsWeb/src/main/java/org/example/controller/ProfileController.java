package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.example.dto.NewsDTO;
import org.example.dto.UserDTO;
import org.example.service.ProfileService;
import org.example.service.Impl.ProfileServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ProfileController {

    @FXML private ImageView avatarImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private ListView<NewsDTO> userPostsList;
    @FXML private Button homeBtn;

    private final ProfileService profileService = new ProfileServiceImpl();

    private static final double AVATAR_SIZE = 80;
    private static final String AVATAR_DIR = "user-data/avatars/";
    private static final String DEFAULT_AVATAR = "/Image/default-thumbnail.jpg";

    private final String currentUserId = "c505cc32-1ea9-47a2-b936-327aaf483dbc";

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
            usernameLabel.setText("Unknown User");
            emailLabel.setText("");
            roleLabel.setText("");
            return;
        }

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(user.getRole());
    }

    private void loadUserNews() {
        List<NewsDTO> news = profileService.getNewsByUserId(currentUserId);

        if (news == null || news.isEmpty()) {
            userPostsList.setPlaceholder(
                    new Label("📰 Bạn chưa có bài viết nào")
            );
            return;
        }

        ObservableList<NewsDTO> data =
                FXCollections.observableArrayList(news);
        userPostsList.setItems(data);
    }

    private void setupListView() {
        userPostsList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(NewsDTO item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
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

    @FXML
    private void onHome() {
        System.out.println("🏠 Back to Home");
    }
}
