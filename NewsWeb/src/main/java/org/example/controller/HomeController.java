package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import javafx.geometry.Side;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


//categỏy
import org.example.dto.CategoryDTO;


public class HomeController implements Initializable {

    //nút chủ đề
    @FXML
    private VBox newsContainer;
    @FXML
    private VBox categoryContainer;
    @FXML
    private ScrollPane categoryScroll;


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
    private String currentCategoryCode = null;
    private String userId = null;

    //biến phân trang
    private static int currentPage = 1;
    private static int totalPage = 1;
    @FXML
    private Label pageLabel;

    @FXML
    private Button prevBtn;

    @FXML
    private Button nextBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // filter luôn tồn tại
        masterNewsList = FXCollections.observableArrayList();
        filteredNewsList = new FilteredList<>(masterNewsList, p -> true);
        newsList.setItems(filteredNewsList);
        newsList.setPlaceholder(new Label("Không có bài viết nào!"));
        setupListView();
        // load lại trang trước đó
        reloadNews();
        // tạo hành động cho nút
        setupUserMenu();

        userBtn.setOnAction(e -> {
            if (userMenu.isShowing()) {
                userMenu.hide();
            } else {
                userMenu.show(userBtn, Side.BOTTOM, 0, 0);
            }
        });
    }

    @FXML
    private void showCategoryScreen() {

        // ẩn list news
        newsContainer.setVisible(false);
        newsContainer.setManaged(false);

        // hiện ScrollPane chứa category
        categoryScroll.setVisible(true);
        categoryScroll.setManaged(true);

        categoryContainer.getChildren().clear();

        List<CategoryDTO> categories = homeService.getCategory();

        for (CategoryDTO category : categories) {

            Button btn = new Button(category.getName());
            btn.setMaxWidth(Double.MAX_VALUE);

            btn.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #DDDDDD;
            -fx-padding: 10;
            -fx-font-weight: bold;
            -fx-cursor: hand;
        """);

            btn.setOnAction(e -> {
                currentMode = HomeMode.CATEGORY;
                currentCategoryCode = category.getCode();
                currentPage = 1;
                totalPage = homeService.countTotalPageNews();
                loadPage();

                // quay lại list news
                categoryScroll.setVisible(false);
                categoryScroll.setManaged(false);

                newsContainer.setVisible(true);
                newsContainer.setManaged(true);
            });

            categoryContainer.getChildren().add(btn);
        }
    }

    //sử lý nút phân trang
    @FXML
    private void handleNextPage() {
        if (currentPage < totalPage) {
            currentPage++;
            loadPage();
        }
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            loadPage();
        }
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
        // reset về trang đề xuất mặc định
        currentMode = HomeMode.RECOMMEND;
        currentPage = 1;
        setupUserMenu();
        loadPage();   // reload lại dữ liệu
        System.out.println("Đã đăng xuất");
    }



    @FXML
    private void handleSearch() {

        String keyword = searchField.getText();

        if (keyword == null || keyword.trim().isEmpty()) {
            currentMode = HomeMode.RECOMMEND;
            currentPage = 1;
            reloadNews();
            return;
        }
        currentMode = HomeMode.SEARCH;
        currentPage = 1;
        totalPage = 1;
        List<NewsDTO> result = homeService.searchNews(keyword.trim());
        updateNewsList(result);

        pageLabel.setVisible(false);
        pageLabel.setManaged(false);

        prevBtn.setVisible(false);
        prevBtn.setManaged(false);

        nextBtn.setVisible(false);
        nextBtn.setManaged(false);
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
        RECOMMEND, NEW, HOT, SEARCH, CATEGORY
    }

    private static HomeMode currentMode = HomeMode.RECOMMEND;
    @FXML
    private void loadRecommendNews() {
        currentMode = HomeMode.RECOMMEND;
        currentPage = 1;
        totalPage = 1; // chỉ 1 trang
        loadPage();
    }


    @FXML
    private void loadNewNews() {
        currentMode = HomeMode.NEW;
        currentPage = 1;
        totalPage = homeService.countTotalPageNews();
        loadPage();
    }

    @FXML
    private void loadHotNews() {
        currentMode = HomeMode.HOT;
        currentPage = 1;
        totalPage = homeService.countTotalPageNews();
        loadPage();
    }



    public void reloadNews() {
        switch (currentMode) {
            case NEW -> loadNewNews();
            case HOT -> loadHotNews();
            case CATEGORY -> loadPage();
            default -> loadRecommendNews();
        }
    }

    private void loadPage() {
        List<NewsDTO> news;

        switch (currentMode) {
            case NEW -> news = homeService.getNewsNewByPage(currentPage);
            case HOT -> news = homeService.getHotNewsByPage(currentPage);
            case CATEGORY -> news = homeService.getNewsPageByCategory(currentCategoryCode, currentPage);
            case SEARCH -> news = new ArrayList<>(masterNewsList); // giữ nguyên kết quả search
            default -> news = homeService.getRecommendNews(userId);
        }
        updateNewsList(news);
        if (currentMode == HomeMode.RECOMMEND || currentMode == HomeMode.SEARCH) {
            pageLabel.setVisible(false);
            pageLabel.setManaged(false);
            prevBtn.setVisible(false);
            prevBtn.setManaged(false);
            nextBtn.setVisible(false);
            nextBtn.setManaged(false);

        } else {
            pageLabel.setVisible(true);
            pageLabel.setManaged(true);
            prevBtn.setVisible(true);
            prevBtn.setManaged(true);
            nextBtn.setVisible(true);
            nextBtn.setManaged(true);
            pageLabel.setText("Trang " + currentPage + " / " + totalPage);
            prevBtn.setDisable(currentPage == 1);
            nextBtn.setDisable(currentPage == totalPage);
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
                    -fx-padding: 10;
                    -fx-background-color: white;
                    -fx-border-color: #EAEAEA;
                    -fx-border-width: 0 0 1 0;
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

    private Parent homeRoot;
    private void openNewsDetail(NewsDTO news) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/NewsDetail/news-detail.fxml")
            );
            Parent detailRoot = loader.load();

            NewsDetailController controller = loader.getController();
            controller.setFromProfile(false);
            // lưu root home hiện tại
            controller.setHomeRoot(newsList.getScene().getRoot());
            // truyền controller
            controller.setUserId(userId);
            controller.setNewsId(news.getId());

            Stage stage = (Stage) newsList.getScene().getWindow();
            stage.getScene().setRoot(detailRoot); // không tạo Scene mới
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
