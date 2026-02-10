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


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;

public class HomeController implements Initializable {

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
        loadRecommendNews();
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
    }

    @FXML
    private void loadRecommendNews() {
        updateNewsList(homeService.getRecommendNews(userId));
    }

    @FXML
    private void loadNewNews() {
        updateNewsList(homeService.getNewNews());
    }

    @FXML
    private void loadHotNews() {
        updateNewsList(homeService.getHotNews());
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

            // vào từ homepage.fxml chỉ xem, không chỉnh
            controller.setFromProfile(false);
            controller.setNews(news);

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
