package com.example.apozh.entity;

import jakarta.persistence.*;
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer matchesPlayed = 99;

    @Column(nullable = false)
    private Integer wins = 54;

    @Column(nullable = false)
    private Integer draws = 3;

    @Column(nullable = false)
    private Integer losses = 42;

    @Column(nullable = false)
    private Integer goalsScored = 422;

    @Column(nullable = false)
    private Integer goalsConceded = 334;

    public Statistics() {
    }

    public Statistics(Long id, Integer matchesPlayed, Integer wins, Integer draws, Integer losses, Integer goalsScored, Integer goalsConceded) {
        this.id = id;
        this.matchesPlayed = matchesPlayed;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
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

    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                ", matchesPlayed=" + matchesPlayed +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                ", goalsScored=" + goalsScored +
                ", goalsConceded=" + goalsConceded +
                '}';
    }
}
