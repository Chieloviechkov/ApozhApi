package com.example.apozh.controllers;

import com.example.apozh.Repository.NextGamesTimeRepository;
import com.example.apozh.entity.NextGamesTime;
import com.example.apozh.service.NextGamesTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class NextGamesController {
    private final NextGamesTimeRepository nextGamesTimeRepository;
    private final NextGamesTimeService nextGamesTimeService;
    @Autowired
    public NextGamesController(NextGamesTimeRepository nextGamesTimeRepository, NextGamesTimeService nextGamesTimeService) {
        this.nextGamesTimeRepository = nextGamesTimeRepository;
        this.nextGamesTimeService = nextGamesTimeService;
    }

    @GetMapping("/nextgames")
    public List<NextGamesTime> getSchedule() {
       nextGamesTimeService.fetchAndSaveSchedule();
        return nextGamesTimeRepository.findAll();
    }
}
