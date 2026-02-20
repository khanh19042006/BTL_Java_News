package org.example.dto;

import java.util.UUID;

public class CommentDTO {

    private String id;
    private String content;
    private String authorId;
    private String newsId;
    private String timeUp;
    private String parentId;

    public CommentDTO() {
        this.id = UUID.randomUUID().toString();
    }

    public CommentDTO(String content, String authorId, String newsId, String timeUp, String parentId) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.authorId = authorId;
        this.newsId = newsId;
        this.timeUp = timeUp;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTimeUp() {
        return timeUp;
    }

    public void setTimeUp(String timeUp) {
        this.timeUp = timeUp;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    // Helper
    public boolean isParentComment() {
        return parentId == null;
    }
}
