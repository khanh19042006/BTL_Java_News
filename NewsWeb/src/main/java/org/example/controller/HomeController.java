package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.example.dao.NewsDAO;
import org.example.entity.News;

import java.util.List;

public class HomeController {
    @FXML
    private ListView<News> newsList;

    @FXML
    public void initialize() {
        NewsDAO dao = new NewsDAO();
        List<News> news = dao.getAllNewsSummary();

        newsList.getItems().addAll(news);

        newsList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(News item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(
                            item.getHeadline() + "\n" +
                                    item.getShort_description()
                    );
                }
            }
        });
    }

}
