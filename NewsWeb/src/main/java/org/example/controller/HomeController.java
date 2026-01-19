package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.example.entity.News;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ListView<News> newsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        setupListView();
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
                        "https://picsum.photos/200/120"
                ),
                new News(
                        "link2",
                        "JavaFX quay trở lại",
                        "Công nghệ",
                        "JavaFX được dùng nhiều trong app desktop",
                        "Tech",
                        "19/01/2026",
                        "Nội dung...",
                        "https://picsum.photos/200/121"
                )
        );
    }

    private void setupListView() {
        newsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(News news, boolean empty) {
                super.updateItem(news, empty);

                if (empty || news == null) {
                    setText(null);
                    return;
                }

                setText(
                        news.getHeadline() + "\n"
                                + news.getShort_description() + "\n"
                                + news.getDate()
                );
            }
        });
    }
}
