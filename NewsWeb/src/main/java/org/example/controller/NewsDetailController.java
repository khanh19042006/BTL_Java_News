package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.scene.layout.HBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;

import org.example.dao.CategoryDAO;
import org.example.dto.CategoryDTO;
import org.example.dto.NewsDTO;
import org.example.dao.NewsDAO;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.ListCell;

import java.time.LocalDate;
//import javafx.scene.web.WebView;
//import javafx.scene.web.WebEngine;


public class NewsDetailController {

    @FXML
    private Button backBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label titleLabel;

    @FXML
    private Label contentLabel;
//    @FXML
//    private WebView contentWebView;


    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private DatePicker publishDatePicker;

    @FXML
    private ComboBox<CategoryDTO> categoryBox;

    // thể loại được lấy từ db
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @FXML
    private HBox metaEditBox;

    // chỉnh sửa ảnh
    @FXML private ImageView thumbnailImage;
    @FXML private Button editThumbnailBtn;

    // biến lưu ảnh
    private static final String NEWS_IMAGE_DIR = "user-data/news/";

    private HomeController homeController;
    private ProfileController profileController;
    public void setHomeController(HomeController c) {
        this.homeController = c;
    }
    public void setProfileController(ProfileController c) {
        this.profileController = c;
    }

    // true  -> vào từ trang cá nhân (được sửa)
    // false -> vào từ trang chủ (chỉ xem)
    private boolean fromProfile = true;

    @FXML
    private void initialize() {
        loadCategories();

        // gán sự kiện
        editBtn.setOnAction(e -> switchToEditMode());
        cancelBtn.setOnAction(e -> switchToViewMode());
        saveBtn.setOnAction(e -> saveChanges());
        backBtn.setOnAction(e -> goBack());
        setEditMode(false);
    }


    private void loadCategories() {
        // lấy dữ liệu từ db
        categoryBox.getItems().setAll(categoryDAO.getCategory());

        categoryBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(CategoryDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        categoryBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CategoryDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    // trang chủ / trang cá nhân sẽ gọi hàm này
    public void setFromProfile(boolean fromProfile) {
        this.fromProfile = fromProfile;

        // nếu không vào từ trang cá nhân thì ẩn nút chỉnh sửa
        if (!fromProfile) {
            editBtn.setVisible(false);
            editBtn.setManaged(false);
        }
    }

    // bài báo hiện tại đang được hiển thị / chỉnh sửa
    private NewsDTO news;

    // dao thao tác với bảng news
    private final NewsDAO newsDAO = new NewsDAO();

    // tăng view chỉ khi mở
    private boolean viewed = false;
    public void onOpenNews() {
        if (viewed) return;
        newsDAO.incrementViewCount(news.getId());
        viewed = true;
    }


    public void setNews(NewsDTO news) {
        this.news = news;
        // tăng view
        onOpenNews();

        // hiển thị dữ liệu lên UI
        titleLabel.setText(news.getHeadline());
        contentLabel.setText(news.getContent());
//        contentWebView.getEngine()
//                .loadContent(wrap(news.getContent()));


        publishDatePicker.setValue(
                LocalDate.parse(news.getDate())
        );

        categoryBox.getItems().stream()
                .filter(c -> c.getCode().equals(news.getCategory()))
                .findFirst()
                .ifPresent(categoryBox::setValue);


        // load ảnh
        Path imagePath = Path.of(NEWS_IMAGE_DIR + news.getId() + ".jpg");
        if (Files.exists(imagePath)) {
            thumbnailImage.setImage(
                    new Image(imagePath.toUri().toString())
            );
        }

        // lưu id để dùng khi đổi ảnh
        this.newsId = news.getId();
    }


    // chuyển sang chế độ chỉnh sửa
    private void switchToEditMode() {
        if (news == null) return;

        titleField.setText(news.getHeadline());
        contentArea.setText(news.getContent());

         // convert String → LocalDate
        publishDatePicker.setValue(
                LocalDate.parse(news.getDate())
        );

        // set category theo code
        categoryBox.getItems().stream()
                .filter(c -> c.getCode().equals(news.getCategory()))
                .findFirst()
                .ifPresent(categoryBox::setValue);


        setEditMode(true);
    }

    // hủy chỉnh
    private void switchToViewMode() {
        setEditMode(false);
    }

    private void setEditMode(boolean editing) {
        titleLabel.setVisible(!editing);
        titleLabel.setManaged(!editing);
        contentLabel.setVisible(!editing);
        contentLabel.setManaged(!editing);
//        contentWebView.setVisible(!editing);
//        contentWebView.setManaged(!editing);


        titleField.setVisible(editing);
        titleField.setManaged(editing);
        contentArea.setVisible(editing);
        contentArea.setManaged(editing);

        // meta (ngày + thể loại)
        metaEditBox.setVisible(editing);
        metaEditBox.setManaged(editing);

        // nút
        editBtn.setVisible(!editing && fromProfile);
        editBtn.setManaged(!editing && fromProfile);

        saveBtn.setVisible(editing);
        saveBtn.setManaged(editing);
        cancelBtn.setVisible(editing);
        cancelBtn.setManaged(editing);

        // chỉnh ảnh
        editThumbnailBtn.setVisible(editing);
        editThumbnailBtn.setManaged(editing);
    }

    // quay lại trang trước
    private void goBack() {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();

            FXMLLoader loader;
            Parent root;

            if (fromProfile) {
                loader = new FXMLLoader(
                        getClass().getResource("/Profile/profile.fxml")
                );
                root = loader.load();

                ProfileController controller = loader.getController();
                controller.reloadUserNews();
            } else {
                loader = new FXMLLoader(
                        getClass().getResource("/HomePage/homePage.fxml")
                );
                root = loader.load();

                HomeController controller = loader.getController();
                controller.reloadNews();
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String newsId;
    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    @FXML
    private void onEditThumbnail() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chọn ảnh bài báo");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg"
                )
        );

        File file = chooser.showOpenDialog(
                thumbnailImage.getScene().getWindow()
        );
        if (file == null) return;

        try {
            Files.createDirectories(Path.of(NEWS_IMAGE_DIR));

            Path target = Path.of(
                    NEWS_IMAGE_DIR + newsId + ".jpg"
            );

            // copy ảnh và ghi đè lên ảnh cũ
            Files.copy(
                    file.toPath(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // load lại ảnh lên view
            thumbnailImage.setImage(
                    new Image(target.toUri().toString())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // lưu thay đổi
    // lưu thay đổi
    private void saveChanges() {
        if (news == null) return;

        if (titleField.getText().isBlank()) return;
        if (publishDatePicker.getValue() == null) return;
        if (categoryBox.getValue() == null) return;

        // cập nhật dữ liệu vào DTO
        news.setHeadline(titleField.getText());
        news.setContent(contentArea.getText());
        news.setDate(publishDatePicker.getValue().toString());
        news.setCategory(categoryBox.getValue().getCode());

        // cập nhật DB
        newsDAO.updateNews(news);

        // cập nhật lại UI
        titleLabel.setText(news.getHeadline());
        contentLabel.setText(news.getContent());
//        contentWebView.getEngine()
//                .loadContent(wrap(news.getContent()));


        setEditMode(false);
    }

    // Hiển thị html
//    private String wrap(String content) {
//        return """
//        <html>
//        <head>
//        <style>
//            body {
//                font-family: Arial;
//                padding: 10px;
//            }
//            img {
//                max-width: 100%;
//                height: auto;
//            }
//            iframe {
//                width: 100%;
//                height: 400px;
//            }
//        </style>
//        </head>
//        <body>
//        """ + (content == null ? "" : content) + """
//        </body>
//        </html>
//        """;
//    }


}
