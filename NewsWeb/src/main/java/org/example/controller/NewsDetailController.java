package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.scene.layout.HBox;

import org.example.dao.UserDAO;
import org.example.dto.CategoryDTO;
import org.example.dto.CommentDTO;
import org.example.dto.NewsDTO;
import org.example.dao.NewsDAO;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.dto.UserDTO;
import org.example.service.CommendService;
import org.example.service.HomeService;
import org.example.service.Impl.CommentServiceImpl;
import org.example.service.Impl.HomeServiceimpl;


public class NewsDetailController {
    @FXML
    private Label errorLabel;

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

    private final HomeService homeService = new HomeServiceimpl();

    @FXML
    private HBox metaEditBox;

    @FXML private ImageView thumbnailImage;
    @FXML private Button editThumbnailBtn;

    @FXML
    private VBox commentContainer;
    @FXML
    private TextArea commentInput;
    @FXML
    private Button sendCommentBtn;
    private final CommendService commentService = new CommentServiceImpl();
    private String replyingToCommentId = null;
    private String currentUserId;
    public void setUserId(String userId) {
        this.currentUserId = userId;
    }

    private static final String NEWS_IMAGE_DIR = "user-data/news/";

    private boolean createMode = false;

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
        sendCommentBtn.setOnAction(e -> handleSendComment());
    }


    private void loadCategories() {
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


    public void setFromProfile(boolean fromProfile) {
        this.fromProfile = fromProfile;

        if (!fromProfile) {
            editBtn.setVisible(false);
            editBtn.setManaged(false);
        }
    }

    private NewsDTO news;

    private final NewsDAO newsDAO = new NewsDAO();

    private boolean viewed = false;
    public void onOpenNews() {
        if (viewed) return;
        newsDAO.incrementViewCount(news.getId());
        viewed = true;
    }

    public void setCreateMode(String userId) {
        this.createMode = true;
        this.fromProfile = true;

        this.news = new NewsDTO();

        String newId = java.util.UUID.randomUUID().toString();
        this.news.setId(newId);
        this.news.setAuthorId(userId);
        this.news.setDate(java.time.LocalDate.now().toString());
        this.news.setViews(0);
        UserDAO userDAO = new UserDAO();
        UserDTO user = userDAO.getUserById(userId);
        this.news.setAuthors(user.getUsername());
        this.newsId = newId;
        setEditMode(true);
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

        loadComments();
    }

    private void loadComments() {
        if (news == null) return;
        commentContainer.getChildren().clear();

        var parents = commentService.getParentComments(news.getId());
        for (CommentDTO parent : parents) {
            VBox parentBox = createCommentWithToggle(parent, 0);
            commentContainer.getChildren().add(parentBox);
        }
    }

    private VBox createCommentWithToggle(CommentDTO comment, int level) {

        VBox wrapper = new VBox(5);

        VBox commentBox = createSimpleComment(comment);
        commentBox.setTranslateX(level * 30);

        VBox replyContainer = new VBox(5);
        replyContainer.setVisible(false);
        replyContainer.setManaged(false);

        var replies = commentService.getReplies(comment.getId());

        if (!replies.isEmpty()) {

            Button toggleBtn = new Button("Xem " + replies.size() + " trả lời");

            toggleBtn.setOnAction(e -> {

                boolean isVisible = replyContainer.isVisible();

                replyContainer.setVisible(!isVisible);
                replyContainer.setManaged(!isVisible);

                if (!isVisible && replyContainer.getChildren().isEmpty()) {
                    for (CommentDTO reply : replies) {
                        VBox replyBox = createCommentWithToggle(reply, level + 1);
                        replyContainer.getChildren().add(replyBox);
                    }
                }

                toggleBtn.setText(
                        isVisible ? "Xem " + replies.size() + " trả lời" : "Ẩn trả lời"
                );
            });

            wrapper.getChildren().addAll(commentBox, toggleBtn, replyContainer);

        } else {
            wrapper.getChildren().add(commentBox);
        }
        return wrapper;
    }

    private void addRepliesRecursive(VBox container, CommentDTO parent, int level) {

        var replies = commentService.getReplies(parent.getId());

        for (CommentDTO reply : replies) {

            VBox replyBox = createSimpleComment(reply);
            replyBox.setTranslateX(level * 30);

            container.getChildren().add(replyBox);

            // gọi reply con
            addRepliesRecursive(container, reply, level + 1);
        }
    }

    private VBox createSimpleComment(CommentDTO comment) {
        VBox box = new VBox(4);

        UserDAO userDAO = new UserDAO();
        UserDTO user = userDAO.getUserById(comment.getAuthorId());
        String username = (user != null) ? user.getUsername() : "Unknown";

        Label authorLabel = new Label(username);
        authorLabel.setStyle("-fx-font-weight: bold;");
        Label contentLabel = new Label(comment.getContent());
        Label timeLabel = new Label(comment.getTimeUp());

        contentLabel.setWrapText(true);

        // nút xóa
        Button deleteBtn = new Button("Xóa");
        Button replyBtn = new Button("Trả lời");
        // nếu chưa đăng nhập → ẩn cả 2 nút
        if (currentUserId == null) {
            deleteBtn.setVisible(false);
            deleteBtn.setManaged(false);

            replyBtn.setVisible(false);
            replyBtn.setManaged(false);
        } else {

            // nút trả lời luôn hiện nếu đã đăng nhập
            replyBtn.setVisible(true);

            // nút xóa chỉ hiện nếu là chủ comment hoặc admin
            if (commentService.isOwner(comment.getId(), currentUserId)
                    || isAdmin(currentUserId)) {
                deleteBtn.setVisible(true);
            } else {
                deleteBtn.setVisible(false);
                deleteBtn.setManaged(false);
            }
        }
        replyBtn.setOnAction(e -> {
            replyingToCommentId = comment.getId();
            commentInput.setPromptText("Trả lời " + username + "...");
            commentInput.requestFocus();
        });
        deleteBtn.setOnAction(e -> {
            boolean success = commentService.deleteComment(
                    comment.getId(), currentUserId
            );
            if (success) {
                loadComments();
            }
        });
        box.getChildren().addAll(authorLabel, contentLabel, timeLabel, replyBtn, deleteBtn);
        return box;
    }

    private boolean isAdmin(String userId) {
        if (userId == null) return false;
        UserDAO userDAO = new UserDAO();
        UserDTO user = userDAO.getUserById(userId);
        return user != null &&
                user.getRole().equalsIgnoreCase("admin");
    }
    private void handleSendComment() {
        if (news == null) return;

        if (currentUserId == null) {
            showError("⚠ Bạn cần đăng nhập để bình luận.");
            return;
        }
        String content = commentInput.getText().trim();
        if (content.isEmpty()) return;
        commentService.createComment(
                content, currentUserId, news.getId(), replyingToCommentId
        );
        commentInput.clear();
        loadComments();
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

    // lấy root từ home
    private Parent homeRoot;

    public void setHomeRoot(Parent homeRoot) {
        this.homeRoot = homeRoot;
    }
    // quay lại trang trước
    private void goBack() {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();

            if (fromProfile) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Profile/profile.fxml")
                );
                Parent root = loader.load();

                ProfileController controller = loader.getController();
                if (profileController != null) {
                    controller.setUserId(profileController.getCurrentUserId());
                }

                stage.setScene(new Scene(root));
            }  else {
                if (homeRoot != null) {
                    stage.getScene().setRoot(homeRoot);  // quay lại root cũ
                }
            }
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
        hideError();
        if (titleField.getText().isBlank()) {
            showError("⚠ Vui lòng nhập tiêu đề.");
            return;
        }
        if (shortDescArea.getText().isBlank()) {
            showError("⚠ Vui lòng nhập tóm tắt.");
            return;
        }
        if (contentArea.getText().isBlank()) {
            showError("⚠ Vui lòng nhập nội dung.");
            return;
        }
        if (categoryBox.getValue() == null) {
            showError("⚠ Vui lòng chọn thể loại.");
            return;
        }

        news.setHeadline(titleField.getText());
        news.setContent(contentArea.getText());
        news.setShort_description(shortDescArea.getText());
        news.setCategory(categoryBox.getValue().getCode());

        if (createMode) {
            boolean success = newsDAO.upNews(news);
            if (success) {
                createMode = false;
                goBack(); // quay lại profile
            } else {
                showError("❌ Đăng bài thất bại.");
            }
        } else {
            newsDAO.updateNews(news);
            setNews(news);
            switchToViewMode();
        }
    }
    // kiểm tra thông tin
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}
