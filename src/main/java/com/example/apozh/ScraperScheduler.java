package com.example.apozh;

import com.example.apozh.service.FootballerService;
import com.example.apozh.service.PhotoService;
import com.example.apozh.service.TeamService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ScraperScheduler {

    private final TeamService teamService;
    private final PhotoService photoService;
    private final FootballerService footballerService;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    public ScraperScheduler(TeamService teamService, PhotoService photoService, FootballerService footballerService) {
        this.teamService = teamService;
        this.photoService = photoService;
        this.footballerService = footballerService;
    }

    @Scheduled(fixedRate = 21600000)
    public void scrapeData() {
        footballerService.scrapeFootballers("https://ksl.co.ua/team/1249800/application");

        CompletableFuture<Void> allUpdates = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024623/tables")),
                CompletableFuture.runAsync(() -> teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024490/tables")),
                CompletableFuture.runAsync(() -> teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024540/tables"))
        );

        allUpdates.thenRun(() -> {
            teamService.updateOrSaveTeams("https://ksl.co.ua/tournament/1024666/tables");
            executorService.schedule(() -> photoService.scrapeAndSavePhotos(), 30, TimeUnit.SECONDS);
        });
    }
}
