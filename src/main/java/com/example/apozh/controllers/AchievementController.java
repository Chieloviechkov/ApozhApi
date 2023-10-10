package com.example.apozh.controllers;

import com.example.apozh.Repository.AchievementRepository;
import com.example.apozh.entity.Achievements;
import com.example.apozh.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementRepository achievementRepository;

    @Autowired
    public AchievementController(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    @GetMapping
    public List<Achievements> getAll() {
        return achievementRepository.findAll();
    }
}
