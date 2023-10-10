package com.example.apozh.entity;

import jakarta.persistence.*;
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int matchesPlayed = 99;

    @Column(nullable = false)
    private int wins = 54;

    @Column(nullable = false)
    private int draws = 3;

    @Column(nullable = false)
    private int losses = 42;

    @Column(nullable = false)
    private int goalsScored = 422;

    @Column(nullable = false)
    private int goalsConceded = 334;

    public Statistics() {
    }

    public Statistics(Long id, int matchesPlayed, int wins, int draws, int losses, int goalsScored, int goalsConceded) {
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
    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
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
