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

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "games", nullable = false)
    private int games;

    @Column(name = "goals")
    private int goals;

    @Column(name = "assists")
    private int assists;

    @Column(name = "yellow_cards")
    private int yellowCards;

    @Column(name = "red_cards")
    private int redCards;

    @Column(name = "missed_goals")
    private double missedGoals;

    public Footballer() {
    }

    public Footballer(Long id, String imgUrl, String firstName, String lastName, String position, int number, LocalDate dateOfBirth, int games, int goals, int assists, int yellowCards, int redCards, double missedGoals) {
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

    public String getName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public double getMissedGoals() {
        return missedGoals;
    }

    public void setMissedGoals(double missedGoals) {
        this.missedGoals = missedGoals;
    }

    @Override
    public String toString() {
        return "Footballer{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", name='" + firstName + '\'' +
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
