package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ImageView thumbnailImage;

    @FXML
    private Label titleLabel;

    @FXML
    private Label metaLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    // true  -> vào từ trang cá nhân (được sửa)
    // false -> vào từ trang chủ (chỉ xem)
    private boolean fromProfile = false;

    @FXML
    private void initialize() {
        // dữ liệu demo
        titleLabel.setText("Tiêu đề bài báo demo");
        metaLabel.setText("06/02/2026 · Công nghệ");
        contentLabel.setText("Nội dung chi tiết bài báo hiển thị ở đây.");

        thumbnailImage.setImage(
                new Image(getClass().getResource("/Image/default-thumbnail.jpg").toExternalForm())
        );

        // các sự kiện
        editBtn.setOnAction(e -> switchToEditMode());
        cancelBtn.setOnAction(e -> switchToViewMode());
        saveBtn.setOnAction(e -> saveChanges());
        backBtn.setOnAction(e -> goBack());
    }

    // tang chủ / trang cá nhân sẽ gọi hàm này
    public void setFromProfile(boolean fromProfile) {
        this.fromProfile = fromProfile;

        // nếu không vào từ trang cá nhân thì ẩn nút chỉnh sửa
        if (!fromProfile) {
            editBtn.setVisible(false);
            editBtn.setManaged(false);
        }
    }

    // chuyển chế độ chỉnh sửa
    private void switchToEditMode() {
        titleField.setText(titleLabel.getText());
        contentArea.setText(contentLabel.getText());
        setEditMode(true);
    }

    // hủy chỉnh
    private void switchToViewMode() {
        setEditMode(false);
    }

    // lưu lại
    private void saveChanges() {
        titleLabel.setText(titleField.getText());
        contentLabel.setText(contentArea.getText());
        setEditMode(false);
    }

    private void setEditMode(boolean editing) {
        titleLabel.setVisible(!editing);
        titleLabel.setManaged(!editing);
        contentLabel.setVisible(!editing);
        contentLabel.setManaged(!editing);

        titleField.setVisible(editing);
        titleField.setManaged(editing);
        contentArea.setVisible(editing);
        contentArea.setManaged(editing);

        editBtn.setVisible(!editing && fromProfile);
        editBtn.setManaged(!editing && fromProfile);

        saveBtn.setVisible(editing);
        saveBtn.setManaged(editing);
        cancelBtn.setVisible(editing);
        cancelBtn.setManaged(editing);
    }

    // quay lại trang trước
    private void goBack() {
        if (fromProfile) {
            System.out.println("Quay về trang cá nhân");
            // load profile.fxml
        } else {
            System.out.println("Quay về trang chủ");
            // load homepage.fxml
        }
    }
}
