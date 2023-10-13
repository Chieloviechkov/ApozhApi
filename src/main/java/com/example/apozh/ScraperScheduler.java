package com.example.apozh;

import com.example.apozh.service.FootballerService;
import com.example.apozh.service.PhotoService;
import com.example.apozh.service.TeamService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScraperScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final TeamService teamService;
    private final PhotoService photoService;
    private final FootballerService footballerService;

    public ScraperScheduler(TeamService teamService, PhotoService photoService, FootballerService footballerService) {
        this.teamService = teamService;
        this.photoService = photoService;
        this.footballerService = footballerService;
    }

    public void startScheduling() {
        scheduler.scheduleAtFixedRate(this::scrapeData, 0, 6, TimeUnit.HOURS);
    }
    public void scrapeData() {
        footballerService.scrapeFootballers("https://ksl.co.ua/team/1249800/application");

        CompletableFuture<Void> allUpdates = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024623/tables")),
                CompletableFuture.runAsync(() -> teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024490/tables")),
                CompletableFuture.runAsync(() -> teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024540/tables"))
        );

        allUpdates.thenRun(() -> {
            teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024666/tables");
            photoService.scrapeAndSavePhotos();
        });
    }


}