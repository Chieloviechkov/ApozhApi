package com.example.apozh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "next_games_time")
public class NextGamesTime {
    @Id
    @Column
    private LocalDate date;
    @Column
    private LocalTime time;
    @Column
    private String location;

    public NextGamesTime() {
    }

    public NextGamesTime(LocalDate date, LocalTime time, String location) {
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "NextGamesTime{" +
                "date=" + date +
                ", time=" + time +
                ", location='" + location + '\'' +
                '}';
    }
}
