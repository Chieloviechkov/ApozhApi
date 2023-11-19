package com.example.apozh.controllers;

import com.example.apozh.Repository.TeamRepository;
import com.example.apozh.entity.Teams;
import com.example.apozh.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    @Value("${tournament.scraping.url.platinum2023_4}")
    private String tournamentUrlPlatinum2023_4;

    @Value("${tournament.scraping.url.scl2022_3}")
    private String tournamentUrlScl2022_3;

    @Value("${tournament.scraping.url.kwl2023}")
    private String tournamentUrlKwl2023;

    @Value("${tournament.scraping.url.ksl2023}")
    private String tournamentUrlKsl2023;
    @Autowired
    public TeamController(TeamService teamService, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.teamRepository = teamRepository;
    }
    @GetMapping("/scrape-platinum-2023-4")
    public List<Teams> scrapePlatinum2023_4() {
        return teamService.updateOrSaveTeams(tournamentUrlPlatinum2023_4);
    }
    @GetMapping("/all")
    public List<Teams> all(){
        return teamRepository.findAll();
    }
    @GetMapping("/scrape-scl-2022-3")
    public List<Teams> scrapeScl2022_3() {
        return teamService.updateOrSaveTeams(tournamentUrlScl2022_3);
    }

    @GetMapping("/scrape-kwl-2023")
    public List<Teams> scrapeKwl2023() {
        return teamService.updateOrSaveTeams(tournamentUrlKwl2023);
    }

    @GetMapping("/scrape-ksl-2023")
    public List<Teams> scrapeKsl2023() {
        return teamService.updateOrSaveTeams(tournamentUrlKsl2023);
    }
}
