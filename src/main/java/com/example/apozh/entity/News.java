package com.example.apozh.entity;

import jakarta.persistence.*;

@Entity
public class News {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    @Column(name = "photo_news")
    private String photoUrl;
    @Column(name = "string_news")
    private String news;
    @Column(name = "string_mini-news")
    private String miniNews;

    public News() {
    }

    public News(Long id, String photoUrl, String news, String miniNews) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.news = news;
        this.miniNews = miniNews;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMiniNews() {
        return miniNews;
    }

    public void setMiniNews(String miniNews) {
        this.miniNews = miniNews;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", photoUrl='" + photoUrl + '\'' +
                ", news='" + news + '\'' +
                ", miniNews='" + miniNews + '\'' +
                '}';
    }
}
