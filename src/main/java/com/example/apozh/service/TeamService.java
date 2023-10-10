package com.example.apozh.service;

import com.example.apozh.Repository.TeamRepository;
import com.example.apozh.entity.Teams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Teams> updateOrSaveTeams(String url) {
        List<Teams> teamsList = scrapeTeams(url);

        for (Teams newTeam : teamsList) {
            Teams existingTeam = teamRepository.findByName(newTeam.getName());

            if (existingTeam != null) {
                existingTeam.setTournament(newTeam.getTournament());
                existingTeam.setLogoUrl(newTeam.getLogoUrl());
                existingTeam.setMatches(newTeam.getMatches());
                existingTeam.setWins(newTeam.getWins());
                existingTeam.setDraws(newTeam.getDraws());
                existingTeam.setLosses(newTeam.getLosses());
                existingTeam.setGoalsScored(newTeam.getGoalsScored());
                existingTeam.setGoalsConceded(newTeam.getGoalsConceded());
                existingTeam.setPoints(newTeam.getPoints());

                teamRepository.save(existingTeam);
            } else {
                teamRepository.save(newTeam);
            }
        }
        return teamsList;
    }

    public List<Teams> scrapeTeams(String url) {
        List<Teams> teamsList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements teamItems = doc.select(".custom-table__line");

            String tournamentTitle = doc.select(".tournament__title").text();

            for (Element teamItem : teamItems) {
                String teamName = teamItem.select(".custom-table__team-name").text().trim();

                boolean exists = teamsList.stream()
                        .anyMatch(team -> team.getName().equals(teamName));

                if (!exists) {
                    String photoUrl = teamItem.select(".custom-table__img").attr("src");

                    int matches = 0;
                    int wins = 0;
                    int draws = 0;
                    int losses = 0;
                    int goalsScored = 0;
                    int goalsConceded = 0;
                    int points = 0;

                    Elements varElements = teamItem.select(".custom-table__var");
                    if (varElements.size() >= 6) {
                        matches = Integer.parseInt(varElements.get(0).select(".custom-table__content").text().trim());
                        wins = Integer.parseInt(varElements.get(1).select(".custom-table__content").text().trim());
                        draws = Integer.parseInt(varElements.get(2).select(".custom-table__content").text().trim());
                        losses = Integer.parseInt(varElements.get(3).select(".custom-table__content").text().trim());
                        String goalsScoredAndConceded = varElements.get(4).select(".custom-table__content").text().trim();
                        goalsScored = Integer.parseInt(goalsScoredAndConceded.split(" - ")[0].trim());
                        goalsConceded = Integer.parseInt(goalsScoredAndConceded.split(" - ")[1].trim());
                        points = Integer.parseInt(varElements.get(5).select(".custom-table__content").text().trim());
                    }

                    Teams team = new Teams();
                    team.setName(teamName);
                    team.setLogoUrl(photoUrl);
                    team.setMatches(matches);
                    team.setWins(wins);
                    team.setDraws(draws);
                    team.setLosses(losses);
                    team.setGoalsScored(goalsScored);
                    team.setGoalsConceded(goalsConceded);
                    team.setPoints(points);

                    team.setTournament(tournamentTitle);

                    teamsList.add(team);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teamsList;
    }

}

