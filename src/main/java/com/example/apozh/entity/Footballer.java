package com.example.apozh.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "footballer")
public class Footballer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "number")
    private Integer number;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "games")
    private Integer games;

    @Column(name = "goals")
    private Integer goals;

    @Column(name = "assists")
    private Integer assists;

    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @Column(name = "red_cards")
    private Integer redCards;

    @Column(name = "missed_goals")
    private Double missedGoals;

    public Footballer() {
    }

    public Footballer(Long id, String imgUrl, String firstName, String lastName, String position, Integer number, LocalDate dateOfBirth, Integer games, Integer goals, Integer assists, Integer yellowCards, Integer redCards, Double missedGoals) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.number = number;
        this.dateOfBirth = dateOfBirth;
        this.games = games;
        this.goals = goals;
        this.assists = assists;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.missedGoals = missedGoals;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getGames() {
        return games;
    }

    public void setGames(Integer games) {
        this.games = games;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    public Double getMissedGoals() {
        return missedGoals;
    }

    public void setMissedGoals(Double missedGoals) {
        this.missedGoals = missedGoals;
    }

    @Override
    public String toString() {
        return "Footballer{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position='" + position + '\'' +
                ", number=" + number +
                ", dateOfBirth=" + dateOfBirth +
                ", games=" + games +
                ", goals=" + goals +
                ", assists=" + assists +
                ", yellowCards=" + yellowCards +
                ", redCards=" + redCards +
                ", missedGoals=" + missedGoals +
                '}';
    }
}
