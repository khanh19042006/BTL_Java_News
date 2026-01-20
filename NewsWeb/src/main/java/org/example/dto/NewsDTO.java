package org.example.dto;

public class NewsDTO {
    private String headline;
    private String category;    //category_code
    private String short_description;
    private String authors;
    private String date;
    private int view;
    private String content;
    private String thumbnail;

    public NewsDTO() {
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

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
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

    public NewsDTO(String headline, String category, String short_description, String authors, String date, int view, String content, String thumbnail) {
        this.headline = headline;
        this.category = category;
        this.short_description = short_description;
        this.authors = authors;
        this.date = date;
        this.view = view;
        this.content = content;
        this.thumbnail = thumbnail;
    }
}
