package com.example.apozh.controllers;

import com.example.apozh.Repository.LastGamesRepository;
import com.example.apozh.entity.LastGames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lastgames")
public class LastGamesController {

    private final LastGamesRepository lastGamesRepository;

    @Autowired
    public LastGamesController(LastGamesRepository lastGamesRepository) {
        this.lastGamesRepository = lastGamesRepository;
    }

    @GetMapping("/all")
    public List<LastGames> getAllLastGames() {
        return lastGamesRepository.findAll();
    }

}
