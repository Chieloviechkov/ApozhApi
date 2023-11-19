package com.example.apozh;

import com.example.apozh.Repository.*;
import com.example.apozh.bot.MyTelegramBot;
import com.example.apozh.service.FootballerService;
import com.example.apozh.service.PhotoService;
import com.example.apozh.service.StatisticsService;
import com.example.apozh.service.TeamService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class ApozhApplication {
    private final AchievementRepository achievementRepository;
    private final NewsRepository newsRepository;
    private final LastGamesRepository lastGamesRepository;
    private final StatisticsService statisticsService;
    private final TeamRepository teamRepository;
    private final FootballerRepository footballerRepository;
    private final FootballerService footballerService;
    private final NextGamesTimeRepository nextGamesTimeRepository;
    private final TeamService  teamService;
    private final PhotoService photoService;
    public static void main(String[] args) {
        SpringApplication.run(ApozhApplication.class, args);
    }
    public ApozhApplication(AchievementRepository achievementRepository, NewsRepository newsRepository,
                            LastGamesRepository lastGamesRepository, StatisticsService statisticsService, TeamRepository teamRepository,
                            FootballerRepository footballerRepository, FootballerService footballerService, NextGamesTimeRepository nextGamesTimeRepository, TeamService teamService, PhotoService photoService) {
        this.achievementRepository = achievementRepository;
        this.newsRepository = newsRepository;
        this.lastGamesRepository = lastGamesRepository;
        this.statisticsService = statisticsService;
        this.teamRepository = teamRepository;
        this.footballerRepository = footballerRepository;
        this.footballerService = footballerService;
        this.nextGamesTimeRepository = nextGamesTimeRepository;
        this.teamService = teamService;
        this.photoService = photoService;
        ScraperScheduler scraperScheduler = new ScraperScheduler(teamService, photoService, footballerService);

        scraperScheduler.scrapeData();
    }
    @Bean
    public MyTelegramBot myTelegramBot() {
        MyTelegramBot bot = new MyTelegramBot(newsRepository, teamRepository, achievementRepository, statisticsService,
                lastGamesRepository, footballerRepository, footballerService, nextGamesTimeRepository);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return bot;
    }

}

