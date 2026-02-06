package org.example.entity;

public class Commend {
    private String id;
    private String content;
    private String authorId;
    private String newsId;
    private String timeUp;
    private int level;

    public Commend(String id, String content, String authorId, String newsId, String timeUp, int level) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.newsId = newsId;
        this.timeUp = timeUp;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
