package com.example.apozh.Repository;

import com.example.apozh.entity.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievements, String> {
    List<Achievements> findByAchievements(String achievementTitle);
}
