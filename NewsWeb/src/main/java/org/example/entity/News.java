package org.example.entity;

import java.util.UUID;

public class News {
    private String id;
    private String link;
    private String headline;
    private String category;
    private String short_description;
    private String authors;
    private String date;

    public News() {
        this.id = UUID.randomUUID().toString();
    }

    public News(String link, String headline, String category, String short_description, String authors, String date) {
        this.id = UUID.randomUUID().toString();
        this.link = link;
        this.headline = headline;
        this.category = category;
        this.short_description = short_description;
        this.authors = authors;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
}
