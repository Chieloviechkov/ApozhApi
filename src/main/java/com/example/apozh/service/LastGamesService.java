package com.example.apozh.service;

import com.example.apozh.Repository.LastGamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastGamesService {

    private final LastGamesRepository lastGamesRepository;

    @Autowired
    public LastGamesService(LastGamesRepository lastGamesRepository) {
        this.lastGamesRepository = lastGamesRepository;
    }
}
