package com.example.apozh.entity;

import jakarta.persistence.*;

@Entity
public class LastGames {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Teams homeTeam;
    @Column
    private String event;
    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Teams awayTeam;
    @Column
    private int homeTeamGoals;
    @Column
    private int awayTeamGoals;

    public LastGames() {
    }

    public LastGames(Long id, Teams homeTeam, String event, Teams awayTeam, int homeTeamGoals, int awayTeamGoals) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.event = event;
        this.awayTeam = awayTeam;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
    }

    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teams getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Teams homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Teams getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Teams awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeTeamGoals() {
        return homeTeamGoals;
    }

    public void setHomeTeamGoals(int homeTeamGoals) {
        this.homeTeamGoals = homeTeamGoals;
    }

    public int getAwayTeamGoals() {
        return awayTeamGoals;
    }

    public void setAwayTeamGoals(int awayTeamGoals) {
        this.awayTeamGoals = awayTeamGoals;
    }

    @Override
    public String toString() {
        return "LastGames{" +
                "id=" + id +
                ", homeTeam=" + homeTeam +
                ", event='" + event + '\'' +
                ", awayTeam=" + awayTeam +
                ", homeTeamGoals=" + homeTeamGoals +
                ", awayTeamGoals=" + awayTeamGoals +
                '}';
    }
}
