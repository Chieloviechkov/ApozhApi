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
    private Integer matches;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "draws")
    private Integer draws;

    @Column(name = "losses")
    private Integer losses;

    @Column(name = "goals_scored")
    private Integer goalsScored;

    @Column(name = "goals_conceded")
    private Integer goalsConceded;

    @Column(name = "points")
    private Integer points;

    public Teams() {
    }

    public Teams(Long id, String tournament, String name, String logoUrl, Integer matches, Integer wins, Integer draws, Integer losses, Integer goalsScored, Integer goalsConceded, Integer points) {
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

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(Integer goalsScored) {
        this.goalsScored = goalsScored;
    }

    public Integer getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(Integer goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
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