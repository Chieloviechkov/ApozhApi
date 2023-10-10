package com.example.apozh.controllers;

import com.example.apozh.Repository.NewsRepository;
import com.example.apozh.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }
}
