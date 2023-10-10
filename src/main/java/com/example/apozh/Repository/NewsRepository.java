package com.example.apozh.Repository;

import com.example.apozh.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, String> {
    List<News> findByNews(String newsTitle);
}
