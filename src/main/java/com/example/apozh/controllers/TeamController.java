package com.example.apozh.controllers;

import com.example.apozh.Repository.TeamRepository;
import com.example.apozh.entity.Teams;
import com.example.apozh.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    @Autowired
    public TeamController(TeamService teamService, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.teamRepository = teamRepository;
    }
    @GetMapping("/scrape-platinum-2023-4")
    public List<Teams> scrapeOne() {
        return teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024666/tables");
    }
    @GetMapping("/all")
    public List<Teams> all(){
        return teamRepository.findAll();
    }
    @GetMapping("/scrape-scl-2022-3")
    public List<Teams> scrapeTwo() {
        return teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024490/tables");
    }
    @GetMapping("/scrape-kwl-2023")
    public List<Teams> scrapeThree() {
        return teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024540/tables");
    }
    @GetMapping("/scrape-ksl-2023")
    public List<Teams> scrapeFour() {
        return teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024623/tables");
    }
}
