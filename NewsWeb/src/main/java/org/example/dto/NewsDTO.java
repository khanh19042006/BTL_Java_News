package org.example.dto;

public class NewsDTO {
    private String id;
    private String headline;
    private String category;    //category_code
    private String short_description;
    private String authors;
    private String date;
    private int views;
    private String content;
    private String thumbnail;
    private String authorId;

    public NewsDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int view) {
        this.views = view;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public NewsDTO(String id, String headline, String category, String short_description, String authors, String date, int views, String content, String thumbnail, String authorId) {
        this.id = id;
        this.headline = headline;
        this.category = category;
        this.short_description = short_description;
        this.authors = authors;
        this.date = date;
        this.views = views;
        this.content = content;
        this.thumbnail = thumbnail;
        this.authorId = authorId;
    }
}
