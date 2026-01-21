package org.example.controller;

import javafx.beans.binding.Bindings;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ListView<NewsDTO> newsList;

    private final HomeService homeService = new HomeServiceimpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // 1️⃣ ép ListView có chiều cao → KHÔNG còn trắng màn hình
        forceListViewVisible();

        // 2️⃣ setup giao diện item
        setupListView();

        // 3️⃣ load data
        loadNewNews();
    }

    /* ================= FORCE LIST VIEW HEIGHT ================= */
    private void forceListViewVisible() {
        newsList.setFixedCellSize(120);

        newsList.prefHeightProperty().bind(
                Bindings.size(newsList.getItems())
                        .multiply(newsList.getFixedCellSize())
                        .add(5)
        );
    }

    /* ================= LOAD DATA ================= */
    private void loadNewNews() {
        List<NewsDTO> news = homeService.getNewNews();

        System.out.println("Loaded news size = " + (news == null ? 0 : news.size()));

        if (news != null) {
            newsList.getItems().setAll(news);
        }
    }

    /* ================= LIST VIEW UI ================= */
    private void setupListView() {

        newsList.setCellFactory(listView -> new ListCell<>() {

            private final ImageView imageView = new ImageView();
            private final Label title = new Label();
            private final Label description = new Label();
            private final Label date = new Label();

            private final VBox textBox = new VBox(6);
            private final HBox root = new HBox(12);

            {
                imageView.setFitWidth(100);
                imageView.setFitHeight(70);
                imageView.setPreserveRatio(true);

                title.setWrapText(true);
                description.setWrapText(true);

                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                description.setStyle("-fx-font-size: 12px;");
                date.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");

                textBox.getChildren().addAll(title, description, date);
                textBox.setPrefWidth(260);

                root.getChildren().addAll(imageView, textBox);
                root.setStyle("""
                        -fx-padding: 12;
                        -fx-background-color: white;
                        -fx-background-radius: 10;
                        -fx-border-radius: 10;
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

    /* ================= IMAGE ================= */
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
