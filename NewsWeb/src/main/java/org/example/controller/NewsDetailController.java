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
import javafx.scene.control.ComboBox;

import org.example.dto.CategoryDTO;
import org.example.dto.NewsDTO;
import org.example.dao.NewsDAO;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.ListCell;
import org.example.service.HomeService;
import org.example.service.Impl.HomeServiceimpl;


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
    private HBox metaViewBox;
    @FXML private Label dateLabel;
    @FXML private Label categoryLabel;

    @FXML
    private Label contentLabel;


    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private ComboBox<CategoryDTO> categoryBox;

    //tóm tắt
    @FXML
    private Label shortDescLabel;

    @FXML
    private TextArea shortDescArea;


    // thể loại được lấy từ db
    private final HomeService homeService = new HomeServiceimpl();

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
        //load dữ liệu từ db
        categoryBox.getItems().setAll(homeService.getCategory());

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
        //tóm tắt
        shortDescLabel.setText(news.getShort_description());
        shortDescArea.setText(news.getShort_description());


        // date
        dateLabel.setText(news.getDate());


        // category
        String categoryCode = news.getCategory();

        if (categoryCode == null || categoryCode.isBlank()) {
            categoryLabel.setText("Chưa phân loại");
        } else {
            categoryBox.getItems().stream()
                    .filter(c -> c.getCode().equals(categoryCode))
                    .findFirst()
                    .ifPresentOrElse(
                            c -> {
                                categoryBox.setValue(c);
                                categoryLabel.setText(c.getName());
                            },
                            () -> categoryLabel.setText("Chưa phân loại")
                    );
        }

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
        shortDescArea.setText(news.getShort_description());

        // set category theo code
        categoryBox.getItems().stream()
                .filter(c -> c.getCode().equals(news.getCategory()))
                .findFirst()
                .ifPresent(categoryBox::setValue);


        setEditMode(true);
    }

    // hủy chỉnh
    private void switchToViewMode() {
        if (news != null) {
            titleLabel.setText(news.getHeadline());
            contentLabel.setText(news.getContent());
            shortDescLabel.setText(news.getShort_description());
            dateLabel.setText(news.getDate());

            categoryBox.getItems().stream()
                    .filter(c -> c.getCode().equals(news.getCategory()))
                    .findFirst()
                    .ifPresent(c -> categoryLabel.setText(c.getName()));
        }
        setEditMode(false);
    }

    private void setEditMode(boolean editing) {
        titleLabel.setVisible(!editing);
        titleLabel.setManaged(!editing);
        contentLabel.setVisible(!editing);
        contentLabel.setManaged(!editing);

        //tóm tắt
        shortDescLabel.setVisible(!editing);
        shortDescLabel.setManaged(!editing);
        shortDescArea.setVisible(editing);
        shortDescArea.setManaged(editing);


        titleField.setVisible(editing);
        titleField.setManaged(editing);
        contentArea.setVisible(editing);
        contentArea.setManaged(editing);

        // meta VIEW (label ngày + thể loại)
        metaViewBox.setVisible(!editing);
        metaViewBox.setManaged(!editing);
        // meta EDIT (DatePicker + ComboBox)
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
                // truyền lại userId
                if (profileController != null) {
                    controller.setUserId(profileController.getCurrentUserId());
                }
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

        // load lại news mới nhất từ db
        NewsDTO freshNews = newsDAO.getNewsById(newsId);
        setNews(freshNews);
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
    private void saveChanges() {
        if (news == null) return;

        if (titleField.getText().isBlank()) return;
        if (categoryBox.getValue() == null) return;

        // cập nhật dữ liệu vào DTO
        news.setHeadline(titleField.getText());
        news.setContent(contentArea.getText());
        news.setShort_description(shortDescArea.getText());
        news.setCategory(categoryBox.getValue().getCode());

        // cập nhật DB
        newsDAO.updateNews(news);

        // cập nhật lại UI
        titleLabel.setText(news.getHeadline());
        contentLabel.setText(news.getContent());
        shortDescLabel.setText(news.getShort_description());

        // cập nhật ngày
        dateLabel.setText(news.getDate());

        // cập nhật tên thể loại
        categoryLabel.setText(categoryBox.getValue().getName());


        setEditMode(false);
    }

}
