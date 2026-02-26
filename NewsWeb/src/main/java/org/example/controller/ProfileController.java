package org.example.controller;

import io.github.cdimascio.dotenv.Dotenv;
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
import org.example.entity.RoleUpgradeRequest;
import org.example.service.Impl.UpgradeRoleImpl;
import org.example.service.ProfileService;
import org.example.service.Impl.ProfileServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import java.util.List;

// profile menu
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;

// search
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
// bắt sự kiện chuyển sang trang detail
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfileController {

    private static final Dotenv dotenv = Dotenv.load();

    @FXML private ImageView avatarImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private ListView<NewsDTO> userPostsList;

    private ObservableList<NewsDTO> masterNewsList = FXCollections.observableArrayList();
    private FilteredList<NewsDTO> filteredNewsList =
            new FilteredList<>(masterNewsList, p -> true);

    @FXML private TextField searchField;

    @FXML
    private Button homeBtn;
    private ContextMenu homeMenu;

    @FXML private Button roleActionBtn;
    @FXML private Button createPostBtn;

    private final ProfileService profileService = new ProfileServiceImpl();

    private static final double AVATAR_SIZE = 80;
    private static final String AVATAR_DIR = "user-data/avatars/";
    private static final String DEFAULT_AVATAR = "/Image/default-thumbnail.jpg";

    private final UpgradeRoleImpl upgradeRoleService = new UpgradeRoleImpl();
    // hardcode admin
//    private final String currentUserId = dotenv.get("ADMIN_ID");
    private String currentUserId;
    public String getCurrentUserId() {
        return currentUserId;
    }
    public void setUserId(String userId) {
        this.currentUserId = userId;

        loadUserInfo();
        loadUserNews();
        loadAvatar();
    }

    @FXML
    public void initialize() {
        userPostsList.setItems(filteredNewsList);
        userPostsList.setPlaceholder(new Label("📰 Chưa có bài viết"));
        setupListView();
        setupSearch();
        setupHomeMenu();
    }

    private void setupHomeMenu() {

        homeMenu = new ContextMenu();

        MenuItem homeItem = new MenuItem("Trang chủ");
        MenuItem logoutItem = new MenuItem("Đăng xuất");

        homeItem.setOnAction(e -> goHome());
        logoutItem.setOnAction(e -> handleLogout());

        homeMenu.getItems().addAll(homeItem, logoutItem);

        homeBtn.setOnAction(e -> {
            if (homeMenu.isShowing()) {
                homeMenu.hide();
            } else {
                homeMenu.show(homeBtn, Side.BOTTOM, -80, 5);
            }
        });
    }

    private void goHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Homepage/homepage.fxml")
            );

            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setUserId(currentUserId); // giữ đăng nhập

            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Trang chủ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Homepage/homepage.fxml")
            );

            Parent root = loader.load();
            // Không set userId → coi như logout

            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Trang chủ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reloadUserNews() {
        loadUserNews();
    }

    private void loadUserInfo() {
        UserDTO user = profileService.getUserById(currentUserId);

        if (user == null) {
            usernameLabel.setText("Unknown");
            emailLabel.setText("");
            roleLabel.setText("");
            roleActionBtn.setVisible(false);
            createPostBtn.setVisible(false);
            return;
        }

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(user.getRole());

        String role = user.getRole().toUpperCase();

        if (role.equals("USER")) {
            roleActionBtn.setText("Xin cấp quyền");
            roleActionBtn.setVisible(true);
        } else if (role.equals("ADMIN")) {
            roleActionBtn.setText("Danh sách yêu cầu");
            roleActionBtn.setVisible(true);
        } else  {
            roleActionBtn.setVisible(false);
        }

        if (role.equals("ADMIN") || role.equals("JOURNALIST")) {
            createPostBtn.setVisible(true);
        } else {
            createPostBtn.setVisible(false);
        }
    }

    @FXML
    private void onRoleAction() {
        String role = roleLabel.getText().toUpperCase();
        if (role.equals("USER")) {
            boolean success = upgradeRoleService.addUser(currentUserId);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xin cấp quyền");
            alert.setHeaderText(null);

            if (success) {
                alert.setContentText("Yêu cầu cấp quyền đã được gửi thành công!");
            } else {
                alert.setContentText("Bạn đã gửi yêu cầu trước đó trong vòng 30 ngày hoặc không đủ điều kiện!");
            }
            alert.showAndWait();

        } else if (role.equals("ADMIN")) {
            showPermissionRequests();
        }
    }

    private void showPermissionRequests() {
        List<RoleUpgradeRequest> requests = upgradeRoleService.getListUser();

        if (requests.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Danh sách yêu cầu");
            alert.setHeaderText(null);
            alert.setContentText("Hiện chưa có user nào xin quyền.");
            alert.showAndWait();
            return;
        }

        VBox vbox = new VBox(10);

        for (RoleUpgradeRequest req : requests) {
            HBox hbox = new HBox(10);
            UserDTO user = profileService.getUserById(req.getUserId());
            Label username = new Label(user.getUsername());
            Button approveBtn = new Button("Cấp quyền");
            approveBtn.setStyle("-fx-font-size: 12px; -fx-cursor: hand;");

            approveBtn.setOnAction(e -> {
                upgradeRoleService.acpUser(req.getUserId());
                vbox.getChildren().remove(hbox);
            });

            hbox.getChildren().addAll(username, approveBtn);
            vbox.getChildren().add(hbox);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Danh sách yêu cầu");
        alert.setHeaderText("User xin quyền:");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(vbox);
        alert.showAndWait();
    }

    private void loadUserNews() {
        List<NewsDTO> news =
                profileService.getNewsByUserId(currentUserId);

        masterNewsList.setAll(news == null ? List.of() : news);
    }

    @FXML
    private void onCreatePost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewsDetail/news-detail.fxml"));
            Parent root = loader.load();
            NewsDetailController controller = loader.getController();

            controller.setCreateMode(currentUserId);
            controller.setProfileController(this);

            Stage stage = (Stage) userPostsList.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tạo bài viết");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListView() {
        userPostsList.setCellFactory(list -> new ListCell<NewsDTO>() {

            private final ImageView imageView = new ImageView();
            private final Label title = new Label();
            private final Label description = new Label();
            private final Label date = new Label();
            private final Label views = new Label();

            private final VBox textBox = new VBox(6);
            private final HBox root = new HBox(12);
            private final HBox metaBox = new HBox(10);

            {
                imageView.setFitWidth(90);
                imageView.setFitHeight(65);
                imageView.setPreserveRatio(true);

                title.setWrapText(true);
                title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                description.setWrapText(true);
                description.setStyle("-fx-font-size: 12px;");

                date.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");
                views.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");

                metaBox.getChildren().addAll(date, views);

                textBox.getChildren().addAll(title, description, metaBox);
                textBox.setPrefWidth(260);

                root.getChildren().addAll(imageView, textBox);
                root.setStyle("""
                    -fx-padding: 10;
                    -fx-background-color: white;
                    -fx-background-radius: 8;
                    -fx-border-radius: 8;
                    -fx-border-color: #E0E0E0;
                    """);

                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
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
                views.setText("👁 " + news.getViews());
                imageView.setImage(loadImage(news.getThumbnail()));

                setGraphic(root);
            }
        });
        userPostsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                NewsDTO selected = userPostsList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openNewsDetail(selected);
                }
            }
        });
    }

    private void openNewsDetail(NewsDTO news) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/NewsDetail/news-detail.fxml")
            );

            Parent detailRoot = loader.load();
            NewsDetailController controller = loader.getController();

            controller.setUserId(currentUserId);
            controller.setFromProfile(true);
            controller.setNewsId(news.getId());
            controller.setProfileController(this);

            Stage stage = (Stage) userPostsList.getScene().getWindow();
            stage.setScene(new Scene(detailRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String keyword = newText == null ? "" : newText.toLowerCase().trim();

            filteredNewsList.setPredicate(news -> {
                if (keyword.isEmpty()) return true;

                return (news.getHeadline() != null &&
                        news.getHeadline().toLowerCase().contains(keyword))
                        || (news.getShort_description() != null &&
                        news.getShort_description().toLowerCase().contains(keyword));
            });
        });
    }

}
