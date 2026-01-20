package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.entity.News;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ListView<News> newsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListView();
        loadData();
    }

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
                title.setStyle("""
                        -fx-font-size: 15px;
                        -fx-font-weight: bold;
                        -fx-text-fill: #222;
                        """);

                description.setWrapText(true);
                description.setStyle("""
                        -fx-font-size: 12px;
                        -fx-text-fill: #666;
                        """);

                date.setStyle("""
                        -fx-font-size: 11px;
                        -fx-text-fill: #999;
                        """);

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

                root.setOnMouseEntered(e -> root.setStyle("""
                        -fx-padding: 12;
                        -fx-background-color: #F7F7F7;
                        -fx-background-radius: 10;
                        -fx-border-radius: 10;
                        -fx-border-color: #CCCCCC;
                        """));

                root.setOnMouseExited(e -> root.setStyle("""
                        -fx-padding: 12;
                        -fx-background-color: white;
                        -fx-background-radius: 10;
                        -fx-border-radius: 10;
                        -fx-border-color: #E0E0E0;
                        """));
            }

            @Override
            protected void updateItem(News news, boolean empty) {
                super.updateItem(news, empty);

                if (empty || news == null) {
                    setGraphic(null);
                    return;
                }

                title.setText(news.getHeadline());
                description.setText(news.getShort_description());
                date.setText("🕒 " + news.getDate());

                Image image;
                try {
                    image = new Image(getClass().getResourceAsStream(news.getThumbnail()));
                } catch (Exception e) {
                    image = new Image(getClass().getResourceAsStream("/Image/default-thumbnail.jpg"));
                }

                imageView.setImage(image);
                setGraphic(root);
            }
        });
    }

    private void loadData() {
        newsList.getItems().addAll(
                new News(
                        "link1",
                        "Giá vàng hôm nay tăng mạnh",
                        "Kinh tế",
                        "Giá vàng trong nước tăng cao nhất 10 năm",
                        "PV",
                        "19/01/2026",
                        "Nội dung...",
                        "/Image/default-thumbnail.jpg"
                ),
                new News(
                        "link2",
                        "JavaFX quay trở lại",
                        "Công nghệ",
                        "JavaFX được dùng nhiều trong app desktop",
                        "Tech",
                        "19/01/2026",
                        "Nội dung...",
                        "/Image/default-thumbnail.jpg"
                )
        );
    }
}
