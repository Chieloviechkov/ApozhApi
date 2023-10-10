package com.example.apozh.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tournament;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "matches")
    private int matches;

    @Column(name = "wins")
    private int wins;

    @Column(name = "draws")
    private int draws;

    @Column(name = "losses")
    private int losses;

    @Column(name = "goals_scored")
    private int goalsScored;

    @Column(name = "goals_conceded")
    private int goalsConceded;

    @Column(name = "points")
    private int points;

    public Teams() {
    }
    public Teams(Long id, String tournament, String name, String logoUrl, int matches, int wins, int draws, int losses, int goalsScored, int goalsConceded, int points) {
        this.id = id;
        this.tournament = tournament;
        this.name = name;
        this.logoUrl = logoUrl;
        this.matches = matches;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
        this.points = points;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Teams{" +
                "id=" + id +
                ", tournament='" + tournament + '\'' +
                ", name='" + name + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", matches=" + matches +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                ", goalsScored=" + goalsScored +
                ", goalsConceded=" + goalsConceded +
                ", points=" + points +
                '}';
    }
}