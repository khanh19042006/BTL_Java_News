package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.dto.NewsDTO;
import org.example.service.HomeService;
import org.example.service.Impl.HomeServiceimpl;

// bắt sự kiện chuyển sang trang detail
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.NewsDetailController;

// sử dụng nemu
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;

public class HomeController implements Initializable {

    // tạo menu
    @FXML
    private Button userBtn;
    private ContextMenu userMenu;

    @FXML
    private TextField searchField;
    // danh sách gốc
    private ObservableList<NewsDTO> masterNewsList;

    // danh sách đã lọc
    private FilteredList<NewsDTO> filteredNewsList;

    @FXML
    private ListView<NewsDTO> newsList;

    private final HomeService homeService = new HomeServiceimpl();
    private String userId = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // filter luôn tồn tại
        masterNewsList = FXCollections.observableArrayList();
        filteredNewsList = new FilteredList<>(masterNewsList, p -> true);
        newsList.setItems(filteredNewsList);
        newsList.setPlaceholder(new Label("Không có bài viết nào!"));
        setupListView();
        setupSearch();
        // load lại trang trước đó
        reloadNews();
        // tạo hành động cho nút
        setupUserMenu();

        userBtn.setOnAction(e -> {
            if (userMenu.isShowing()) {
                userMenu.hide();
            } else {
                userMenu.show(userBtn, Side.BOTTOM, -120, 5);
            }
        });


    }

    // hàm menu
    private void setupUserMenu() {

        if (userMenu == null) {
            userMenu = new ContextMenu();
        } else {
            userMenu.getItems().clear();
        }
        userMenu.setAutoHide(true);

        boolean isLoggedIn = userId != null;

        if (isLoggedIn) {
            MenuItem profileItem = new MenuItem("Trang cá nhân");
            MenuItem logoutItem = new MenuItem("Đăng xuất");

            profileItem.setOnAction(e -> openProfile());
            logoutItem.setOnAction(e -> logout());

            userMenu.getItems().addAll(profileItem, logoutItem);
        } else {
            MenuItem profileItem = new MenuItem("Trang cá nhân");
            profileItem.setDisable(true);

            MenuItem loginItem = new MenuItem("Đăng nhập");
            MenuItem registerItem = new MenuItem("Đăng ký");

            loginItem.setOnAction(e -> openLogin());
            registerItem.setOnAction(e -> openRegister());

            userMenu.getItems().addAll(profileItem, loginItem, registerItem);
        }
    }
    // các hành động
    private void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Profile/profile.fxml")
            );
            Parent root = loader.load();

            ProfileController controller = loader.getController();
            controller.setUserId(userId); // truyền userId

            Stage stage = (Stage) userBtn.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openLogin() {
        try {
            // lớp đọc file fxml
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Login/login.fxml")
            );

            Parent root = loader.load();

            // lấy cửa sổ hiện tại
            Stage stage = (Stage) userBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Register/register.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) userBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng ký");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        userId = null;
        setupUserMenu(); // refresh menu sau khi logout
        System.out.println("Đã đăng xuất");
    }


    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String keyword = newText == null ? "" : newText.trim().toLowerCase();
            filteredNewsList.setPredicate(news -> {
                if (keyword.isEmpty()) return true;
                return containsIgnoreCase(news.getHeadline(), keyword)
                        || containsIgnoreCase(news.getShort_description(), keyword);
            });
        });
    }

    private boolean containsIgnoreCase(String text, String keyword) {
        return text != null && text.toLowerCase().contains(keyword);
    }

    public void setUserId(String userId) {
        this.userId = userId;
        setupUserMenu();
    }

    // biến nhớ trạng thái home đang ở chế độ nào
    public enum HomeMode {
        RECOMMEND, NEW, HOT
    }
    private static HomeMode currentMode = HomeMode.RECOMMEND;
    @FXML
    private void loadRecommendNews() {
        currentMode = HomeMode.RECOMMEND;
        updateNewsList(homeService.getRecommendNews(userId));
    }

    @FXML
    private void loadNewNews() {
        currentMode = HomeMode.NEW;
        updateNewsList(homeService.getNewNews());
    }

    @FXML
    private void loadHotNews() {
        currentMode = HomeMode.HOT;
        updateNewsList(homeService.getHotNews());
    }

    public void reloadNews() {
        switch (currentMode) {
            case NEW -> loadNewNews();
            case HOT -> loadHotNews();
            default -> loadRecommendNews();
        }
    }
    // đổ dữ liệu lên UI
    private void updateNewsList(List<NewsDTO> news) {
        masterNewsList.setAll(news == null ? List.of() : news);
    }

    private void setupListView() {
        newsList.setCellFactory(listView -> {
            ListCell<NewsDTO> cell = new ListCell<>() {
                private final ImageView imageView = new ImageView();
                private final Label title = new Label();
                private final Label description = new Label();
                private final Label date = new Label();
                private final VBox textBox = new VBox(6);
                private final HBox root = new HBox(12);
                private final Label views = new Label();

                {
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(70);
                    imageView.setPreserveRatio(true);

                    title.setWrapText(true);
                    title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                    description.setWrapText(true);
                    description.setStyle("-fx-font-size: 12px;");
                    date.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");

                    HBox metaBox = new HBox(10, date, views);
                    textBox.getChildren().addAll(title, description, metaBox);

                    textBox.setPrefWidth(260);

                    views.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");

                    root.getChildren().addAll(imageView, textBox);
                    root.setStyle("""
                -fx-padding: 12;
                -fx-background-color: white;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: #E0E0E0;
            """);
                    setGraphic(root);
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
            };

            return cell;
        });
        newsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                NewsDTO selected =
                        newsList.getSelectionModel().getSelectedItem();
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
            Parent root = loader.load();
            NewsDetailController controller = loader.getController();
            controller.setFromProfile(false);
            controller.setHomeController(this);
            // truyền ID
            controller.setNewsId(news.getId());

            Stage stage = (Stage) newsList.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image loadImage(String path) {
        try {
            if (path == null || path.isBlank()) throw new Exception();
            return new Image(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            return new Image(
                    getClass().getResourceAsStream("/Image/default-thumbnail.jpg")
            );
        }
    }
}
