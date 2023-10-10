package com.example.apozh.entity;


import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url",nullable = false)
    private String imageUrl;
    public Photo() {
    }

    public Photo(Long id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
