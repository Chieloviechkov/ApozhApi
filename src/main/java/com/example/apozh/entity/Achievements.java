package com.example.apozh.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Achievements {
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    @CollectionTable(name = "achievement_photos")
    private List<String> photoUrls;
    private String achievements;
    private String miniAchievements;

    public Achievements() {
    }

    public Achievements(Long id, List<String> photoUrls, String achievements, String miniAchievements) {
        this.id = id;
        this.photoUrls = photoUrls;
        this.achievements = achievements;
        this.miniAchievements = miniAchievements;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getMiniAchievements() {
        return miniAchievements;
    }

    public void setMiniAchievements(String miniAchievements) {
        this.miniAchievements = miniAchievements;
    }

    @Override
    public String toString() {
        return "Achievements{" +
                "id=" + id +
                ", photoUrls=" + photoUrls +
                ", achievements='" + achievements + '\'' +
                ", miniAchievements='" + miniAchievements + '\'' +
                '}';
    }
}
