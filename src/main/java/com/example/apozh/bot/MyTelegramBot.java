package com.example.apozh.bot;

import com.example.apozh.Repository.*;
import com.example.apozh.entity.*;
import com.example.apozh.service.FootballerService;
import com.example.apozh.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final NewsRepository newsRepository;
    private final TeamRepository teamRepository;
    private final AchievementRepository achievementRepository;
    private final StatisticsService statisticsService;
    private final LastGamesRepository lastGamesRepository;
    private final FootballerRepository footballerRepository;
    private final FootballerService footballerService;
    private final NextGamesTimeRepository nextGamesTimeRepository;
    private Map<Long, UserState> userStates = new HashMap<>();
    private Map<Long, News> newsData = new HashMap<>();
    private Map<Long, Achievements> achievementsData = new HashMap<>();

    @Autowired
    public MyTelegramBot(NewsRepository newsRepository, TeamRepository teamRepository, AchievementRepository achievementRepository,
                         StatisticsService statisticsService, LastGamesRepository lastGamesRepository, FootballerRepository footballerRepository,
                         FootballerService footballerService, NextGamesTimeRepository nextGamesTimeRepository) {
        this.newsRepository = newsRepository;
        this.teamRepository = teamRepository;
        this.achievementRepository = achievementRepository;
        this.statisticsService = statisticsService;
        this.lastGamesRepository = lastGamesRepository;
        this.footballerRepository = footballerRepository;
        this.footballerService = footballerService;
        this.nextGamesTimeRepository = nextGamesTimeRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            UserState userState = userStates.getOrDefault(chatId, UserState.DEFAULT);

            if (message.hasText()) {
                String text = message.getText();
                switch (text) {
                    case "/start":
                        checkAndSendNotifications(chatId);
                        break;
                    case "/news":
                        userStates.put(chatId, UserState.ENTER_NEWS_TITLE);
                        News news = new News();
                        newsData.put(chatId, news);
                        sendTextMessage(chatId, "Який заголовок новини?");
                        break;
                    case "/deletenews":
                        userStates.put(chatId, UserState.DELETE_NEWS);
                        sendTextMessage(chatId, "Введіть заголовок новини, яку ви хочете видалити:");
                        break;
                    case "/achievements":
                        userStates.put(chatId, UserState.ENTER_ACHIEVEMENT_TITLE);
                        Achievements achievements = new Achievements();
                        achievementsData.put(chatId, achievements);
                        sendTextMessage(chatId, "Який заголовок досягнення?");
                        break;
                    case "/deleteachievement":
                        userStates.put(chatId, UserState.DELETE_ACHIEVEMENT);
                        sendTextMessage(chatId, "Введіть заголовок досягнення, яке ви хочете видалити:");
                        break;
                    case "/loadmatch":
                        userStates.put(chatId, UserState.ENTER_MATCH_DETAILS);
                        sendTextMessage(chatId, "Як зіграли? (Приклад - FC APOZH 3-2 FC. GOAT | 30’ Савченко І (Дума І), 40’ Савченко І - червона картка");
                        break;
                    case "/deletelastgame":
                        deleteLastGameAndStatistics(chatId);
                        break;
                    case "/deletetatisticforplayer":
                        sendTextMessage(chatId, "Для видалення статистики гравця треба ввести повідомлення в форматі - 'Перша буква імені Прізвище Матчі Голи Асисти ЖК ЧК'");
                        userStates.put(chatId, UserState.ENTER_FOOTBALLER_TO_DELETE);
                        break;
                    case "/loadmaincast":
                        sendTextMessage(chatId, "Введіть основний склад на матч в форматі 'Іван Савченко, Олександр Алімов'");
                        userStates.put(chatId, UserState.ENTER_MAIN_CAST);
                        break;
                    case "/loadgoalkeeper":
                        sendTextMessage(chatId, "Будь ласка, введіть прізвище та ім'я воротаря та кількість пропущених голів у сьогоднішній грі в форматі: Прізвище Ім'я Голи");
                        userStates.put(chatId, UserState.ENTER_GOALKEEPER);
                        break;
                    case "/loadstatisticforplayer":
                        sendTextMessage(chatId, "Для додавання статистики гравця треба ввести повідомлення в форматі - 'Перша буква імені Прізвище Матчі Голи Асисти ЖК ЧК'");
                        userStates.put(chatId, UserState.ENTER_PLAYER_STATISTIC);
                        break;
                    default:
                        handleUserInput(chatId, text, userState);
                        break;
                }
            } else if (message.hasDocument()) {
                if (userState == UserState.ENTER_NEWS_PHOTO) {
                    String newsTitle = newsData.containsKey(chatId) ? newsData.get(chatId).getNews() : "";
                    String newsDescription = newsData.containsKey(chatId) ? newsData.get(chatId).getMiniNews() : "";
                    String fileId = message.getDocument().getFileId();
                    handleDocumentInput(chatId, newsTitle, newsDescription, fileId);
                } else if (userState == UserState.ENTER_ACHIEVEMENT_PHOTO) {
                    String fileId = message.getDocument().getFileId();
                    handleDocumentInput(chatId, fileId);
                }
            }

        }
    }
    public void checkAndSendNotifications(Long chatId) {
        List<NextGamesTime> nextGames = nextGamesTimeRepository.findAll();

        for (NextGamesTime nextGame : nextGames) {
            LocalDateTime gameDateTime = LocalDateTime.of(nextGame.getDate(), nextGame.getTime());
            LocalDateTime currentDateTime = LocalDateTime.now();
            long hoursUntilGame = currentDateTime.until(gameDateTime, ChronoUnit.HOURS);
            System.out.println(hoursUntilGame   );
            if (hoursUntilGame <= 1.5 && hoursUntilGame > 0) {
                sendTextMessage(chatId,"До гри " + nextGame.getLocation() + " залишилось менш ніж 1.5 години, збирайся. "
                        + nextGame.getDate() + " " + nextGame.getTime());
            }
        }
    }
    private void handleLastGamesDetails(Long chatId, String matchDetails) {
        String[] parts = matchDetails.split("\\|");

        if (parts.length == 2) {
            String teamAndScore = parts[0].trim();
            String eventAndDescription = parts[1].trim();

            Pattern pattern = Pattern.compile("(.+) (\\d+)-(\\d+) (.+)");
            Matcher matcher = pattern.matcher(teamAndScore);

            if (matcher.matches()) {
                String team1Name = matcher.group(1);
                int scoreTeam1 = Integer.parseInt(matcher.group(2));
                int scoreTeam2 = Integer.parseInt(matcher.group(3));
                String team2Name = matcher.group(4);

                Teams homeTeam = teamRepository.findByName(team1Name);
                Teams awayTeam = teamRepository.findByName(team2Name);

                if (homeTeam != null && awayTeam != null) {
                    LastGames lastGame = new LastGames();
                    lastGame.setHomeTeam(homeTeam);
                    lastGame.setAwayTeam(awayTeam);
                    lastGame.setHomeTeamGoals(scoreTeam1);
                    lastGame.setAwayTeamGoals(scoreTeam2);
                    lastGame.setEvent(eventAndDescription);
                    lastGamesRepository.save(lastGame);
                    sendTextMessage(chatId, "Інформація про останню гру збережена!");
                } else {
                    sendTextMessage(chatId, "Одну з команд не знайдено. Будь ласка, переконайтеся, що обидві команди зареєстровані.");
                }
            } else {
                sendTextMessage(chatId, "Помилка при розборі деталей матчу. Будь ласка, введіть валідний формат (наприклад, 'Team1 3-2 Team2 | Савченко І. (Дума І.) Савченко І. - червона картка').");
            }
        } else {
            sendTextMessage(chatId, "Неправильний формат введення. Будь ласка, використовуйте вертикальну межу (|) для розділення події та додаткового тексту.\n");
        }
    }

    private void handleDocumentInput(Long chatId, String newsTitle, String newsDescription, String fileId) {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(fileId);
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFileMethod);
            String fileUrl = file.getFileUrl(getBotToken());
            saveNewsWithPhoto(chatId, newsTitle, newsDescription, fileUrl);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        sendTextMessage(chatId, "Дякую, фотографію прикріплені до новини!");
        userStates.remove(chatId);
        newsData.remove(chatId);
    }

    private void deleteLastGameAndStatistics(Long chatId) {
        LastGames lastGame = lastGamesRepository.findTopByOrderByIdDesc();
        if (lastGame != null && lastGame.getHomeTeam() != null && lastGame.getAwayTeam() != null) {
            String homeTeamName = lastGame.getHomeTeam().getName();
            String awayTeamName = lastGame.getAwayTeam().getName();
            int homeTeamGoals = lastGame.getHomeTeamGoals();
            int awayTeamGoals = lastGame.getAwayTeamGoals();

            minusData(chatId, homeTeamName, homeTeamGoals, awayTeamName, awayTeamGoals);
            lastGamesRepository.delete(lastGame);
            sendTextMessage(chatId, "Остання гра та пов'язана статистика видалені.");
        } else {
            sendTextMessage(chatId, "Немає останніх ігор для видалення або дані неповні.");
        }

    }

    private void handleUserInput(Long chatId, String text, UserState userState) {
        Achievements achievements = achievementsData.get(chatId);
        switch (userState) {
            case ENTER_NEWS_TITLE:
                News news = newsData.get(chatId);
                news.setNews(text);
                newsData.put(chatId, news);
                userStates.put(chatId, UserState.ENTER_NEWS_DESCRIPTION);
                sendTextMessage(chatId, "Тепер введи опис новини:");
                break;
            case ENTER_NEWS_DESCRIPTION:
                newsData.get(chatId).setMiniNews(text);
                userStates.put(chatId, UserState.ENTER_NEWS_PHOTO);
                sendTextMessage(chatId, "Будь ласка, прикріпи фотографію.\nТреба найбільш поширені формати, ну і надсилати файлом.");
                break;
            case DELETE_NEWS:
                String newsTitleToDelete = text;
                deleteNewsByTitle(chatId, newsTitleToDelete);
                userStates.remove(chatId);
                break;
            case ENTER_ACHIEVEMENT_TITLE:
                achievements.setAchievements(text);
                userStates.put(chatId, UserState.ENTER_ACHIEVEMENT_DESCRIPTION);
                sendTextMessage(chatId, "Тепер введи опис досягнення:");
                break;
            case ENTER_ACHIEVEMENT_DESCRIPTION:
                achievements.setMiniAchievements(text);
                userStates.put(chatId, UserState.ENTER_ACHIEVEMENT_PHOTO);
                sendTextMessage(chatId, "Скинь фотографії файлом. Коли закінчиш, введи /done.");
                break;
            case ENTER_ACHIEVEMENT_PHOTO:
                if (!text.equals("/done")) {
                    String fileId = text;
                    handleDocumentInput(chatId, fileId);
                } else {
                    String achievementDescription = achievementsData.get(chatId).getMiniAchievements();
                    List<String> photoUrls = achievementsData.get(chatId).getPhotoUrls();
                    saveAchievementWithPhotos(chatId, achievementDescription, photoUrls);
                    userStates.remove(chatId);
                    sendTextMessage(chatId, "Досягнення з фото збережено.");
                }
                break;
            case DELETE_ACHIEVEMENT:
                String achievementTitleToDelete = text;
                deleteAchievementByTitle(chatId, achievementTitleToDelete);
                userStates.remove(chatId);
                break;
            case ENTER_MATCH_DETAILS:
                handleMatchDetails(chatId, text);
                userStates.remove(chatId);
                break;
            case ENTER_FOOTBALLER_TO_DELETE:
                String[] playerStats = text.split(" ");
                if (playerStats.length >= 6) {
                    String playerName = playerStats[0] + " " + playerStats[1];
                    int matches = Integer.parseInt(playerStats[2]);
                    int goalsScored = Integer.parseInt(playerStats[3]);
                    int assists = Integer.parseInt(playerStats[4]);
                    int yellowCards = Integer.parseInt(playerStats[5]);
                    int redCards = Integer.parseInt(playerStats[6]);
                    footballerService.deletePlayerStatistics(playerName, matches, goalsScored, assists, yellowCards, redCards);
                    sendTextMessage(chatId, "Статистика гравця " + playerName + " успішно видалена.");
                } else {
                    sendTextMessage(chatId, "Невірний формат вводу. Будь ласка, використовуйте формат 'Ім'я Прізвище Голи Асисти ЖК КК'.");
                }
            case ENTER_MAIN_CAST:
            footballerService.updateMatchStatistics(text);
            sendTextMessage(chatId, "Кількість зіграних матчів оновлена!");
            case ENTER_GOALKEEPER:
                String[] userInputParts = text.split(" ");

                if (userInputParts.length == 3) {
                    String lastName = userInputParts[0];
                    String firstName = userInputParts[1];
                    int goals = Integer.parseInt(userInputParts[2]);

                    Footballer goalkeeper = footballerRepository.findByLastNameAndFirstName(lastName, firstName);

                    if (goalkeeper != null) {
                        goalkeeper.setGoals(goals);
                        footballerRepository.save(goalkeeper);
                        sendTextMessage(chatId, "Статистика для голкіпера " + lastName + " " + firstName + " оновлена.");
                    } else {
                        sendTextMessage(chatId, "Голкіпера з ім'ям " + lastName + " " + firstName + " не знайдено в базі.");
                    }
                } else {
                    sendTextMessage(chatId, "Неправильний формат вводу. Введіть прізвище, ім'я та кількість голів через пробіл.");
                }
            case ENTER_PLAYER_STATISTIC:
                String[] playerStat = text.split(" ");
                if (playerStat.length >= 6) {
                    String playerName = playerStat[0] + " " + playerStat[1];
                    int matches = Integer.parseInt(playerStat[2]);
                    int goalsScored = Integer.parseInt(playerStat[3]);
                    int assists = Integer.parseInt(playerStat[4]);
                    int yellowCards = Integer.parseInt(playerStat[5]);
                    int redCards = Integer.parseInt(playerStat[6]);
                    footballerService.updatePlayerStatisticsWithGames(playerName, matches, goalsScored, assists, yellowCards, redCards);
                    sendTextMessage(chatId, "Статистика гравця " + playerName + " успішно додана.");
                } else {
                    sendTextMessage(chatId, "Невірний формат вводу. Будь ласка, використовуйте формат 'Ім'я Прізвище Матчі Голи Асисти ЖК КК'.");
                }
        }
    }

    private void handleMatchDetails(Long chatId, String matchDetails) {
        Pattern pattern = Pattern.compile("(.+) (\\d+)-(\\d+) (.+) (.+)");
        Matcher matcher = pattern.matcher(matchDetails);

        if (matcher.matches()) {
            String team1Name = matcher.group(1);
            int scoreTeam1 = Integer.parseInt(matcher.group(2));
            int scoreTeam2 = Integer.parseInt(matcher.group(3));
            String team2Name = matcher.group(4);

            updateStatistics(chatId, team1Name, scoreTeam1, team2Name, scoreTeam2);
            handleLastGamesDetails(chatId, matchDetails);
            updatePlayerStatisticsFromMatchText(matchDetails);
        } else {
            sendTextMessage(chatId, "Помилка при розборі деталей матчу. Введіть валідний формат (наприклад, 'FC APOZH 3-2 ECF').");
        }
    }

    public void updatePlayerStatisticsFromMatchText(String matchText) {
        String[] parts = matchText.split("\\|");
        String eventsInfo = parts[1].trim();
        String[] eventsArray = eventsInfo.split(",");
        for (String event : eventsArray) {
            String formattedEvent = event.trim();
            if (!formattedEvent.isEmpty()) {
                if (formattedEvent.contains("червона картка")) {
                    String redCardPlayer = removeMinutes(formattedEvent.split("\\(")[0].trim());
                    String[] nameParts = redCardPlayer.split("\\s+");
                    String lastName = nameParts[0];
                    String firstNameInitial = nameParts[1].substring(0, 1);

                    String fullName = firstNameInitial + " " + lastName;

                    footballerService.updatePlayerStatistics(fullName, 0, 0, 0, 1);


                } else if (formattedEvent.contains("жовта картка")) {
                    String yellowCardPlayer = removeMinutes(formattedEvent.split("\\(")[0].trim());
                    String[] nameParts = yellowCardPlayer.split("\\s+");
                    String lastName = nameParts[0];
                    String firstNameInitial = nameParts[1].substring(0, 1);

                    String fullName = firstNameInitial + " " + lastName;

                    footballerService.updatePlayerStatistics(fullName, 0, 0, 1, 0);


                } else {
                    String[] eventParts = formattedEvent.split("\\(");
                    String goalScorerInfo = removeMinutes(eventParts[0].trim());
                    String[] nameParts = goalScorerInfo.split("\\s+");
                    String lastName = nameParts[0];
                    String firstNameInitial = "";
                    if (nameParts.length > 1) {
                        firstNameInitial = nameParts[1].substring(0, 1);
                    }

                    String fullName = firstNameInitial + " " + lastName;

                    footballerService.updatePlayerStatistics(fullName, 1, 0, 0, 0);
                    String assistProvider = "";

                    if (eventParts.length > 1) {
                        assistProvider = eventParts[1].replace(")", "").trim();
                        String[] assistNameParts = assistProvider.split("\\s+");
                        String assistLastName = assistNameParts[0];
                        String assistFirstNameInitial = "";
                        if (assistNameParts.length > 1) {
                            assistFirstNameInitial = assistNameParts[1].substring(0, 1);
                        }

                        String assistFullName = assistFirstNameInitial + " " + assistLastName;

                        footballerService.updatePlayerStatistics(assistFullName, 0, 1, 0, 0);
                    }
                }
            }
        }
    }

    private String removeMinutes(String event) {
        return event.replaceAll("\\d+’", "").trim();
    }

    private void updateStatistics(Long chatId, String team1Name, int scoreTeam1, String team2Name, int scoreTeam2) {
        Teams team1 = teamRepository.findByName(team1Name);
        Teams team2 = teamRepository.findByName(team2Name);
        if (team1 != null || team2 != null) {
            plusData(chatId, team1Name, scoreTeam1, team2Name, scoreTeam2);
        } else {
            sendTextMessage(chatId, "Одну з команд не знайдено. Будь ласка, переконайтеся, що обидві команди зареєстровані.");
        }
    }

    private void minusData(Long chatId, String team1Name, int scoreTeam1, String team2Name, int scoreTeam2) {
        Statistics apoStatistics = statisticsService.getApozhStatistics();

        if (apoStatistics.getMatchesPlayed() > 0) {
            apoStatistics.setMatchesPlayed(apoStatistics.getMatchesPlayed() - 1);
            apoStatistics.setGoalsScored(apoStatistics.getGoalsScored() - scoreTeam1);
            apoStatistics.setGoalsConceded(apoStatistics.getGoalsConceded() - scoreTeam2);

            if (team1Name.equalsIgnoreCase("FC APOZH")) {
                if (scoreTeam1 > scoreTeam2) {
                    apoStatistics.setWins(apoStatistics.getWins() - 1);
                } else if (scoreTeam1 < scoreTeam2) {
                    apoStatistics.setLosses(apoStatistics.getLosses() - 1);
                } else {
                    apoStatistics.setDraws(apoStatistics.getDraws() - 1);
                }
            } else if (team2Name.equalsIgnoreCase("FC APOZH")) {
                if (scoreTeam2 > scoreTeam1) {
                    apoStatistics.setWins(apoStatistics.getWins() - 1);
                } else if (scoreTeam2 < scoreTeam1) {
                    apoStatistics.setLosses(apoStatistics.getLosses() - 1);
                } else {
                    apoStatistics.setDraws(apoStatistics.getDraws() - 1);
                }
            }

            statisticsService.saveStatistics(apoStatistics);
            sendTextMessage(chatId, "Статистика обновлена!");
        } else {
            sendTextMessage(chatId, "Немає матчів у статистиці для скасування.");
        }
    }

    private void plusData(Long chatId, String team1Name, int scoreTeam1, String team2Name, int scoreTeam2) {
        Statistics apoStatistics = statisticsService.getApozhStatistics();

        apoStatistics.setMatchesPlayed(apoStatistics.getMatchesPlayed() + 1);

        apoStatistics.setGoalsScored(apoStatistics.getGoalsScored() + scoreTeam1);
        apoStatistics.setGoalsConceded(apoStatistics.getGoalsConceded() + scoreTeam2);

        if (team1Name.equalsIgnoreCase("FC APOZH")) {
            if (scoreTeam1 > scoreTeam2) {
                apoStatistics.setWins(apoStatistics.getWins() + 1);
            } else if (scoreTeam1 < scoreTeam2) {
                apoStatistics.setLosses(apoStatistics.getLosses() + 1);
            } else {
                apoStatistics.setDraws(apoStatistics.getDraws() + 1);
            }
        } else if (team2Name.equalsIgnoreCase("FC APOZH")) {
            if (scoreTeam2 > scoreTeam1) {
                apoStatistics.setWins(apoStatistics.getWins() + 1);
            } else if (scoreTeam2 < scoreTeam1) {
                apoStatistics.setLosses(apoStatistics.getLosses() + 1);
            } else {
                apoStatistics.setDraws(apoStatistics.getDraws() + 1);
            }
        }
        statisticsService.saveStatistics(apoStatistics);
        sendTextMessage(chatId, "Статистика обновлена!");
    }

    private void saveNewsWithPhoto(Long chatId, String newsTitle, String newsDescription, String photoUrl) {
        News news = newsData.get(chatId);
        news.setPhotoUrl(photoUrl);
        newsRepository.save(news);
    }

    private void handleDocumentInput(Long chatId, String fileId) {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(fileId);
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFileMethod);
            String fileUrl = file.getFileUrl(getBotToken());

            Achievements achievements = achievementsData.get(chatId);
            if (achievements.getPhotoUrls() == null) {
                achievements.setPhotoUrls(new ArrayList<>());
            }
            achievements.getPhotoUrls().add(fileUrl);

            sendTextMessage(chatId, "Фото прикріпрелене. Відправте ще фотографії чи введіть /done.");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void saveAchievementWithPhotos(Long chatId, String achievementDescription, List<String> photoUrls) {
        Achievements achievements = achievementsData.get(chatId);
        achievements.setMiniAchievements(achievementDescription);
        achievements.setPhotoUrls(photoUrls);
        achievementRepository.save(achievements);
    }

    private void deleteNewsByTitle(Long chatId, String newsTitle) {
        Iterable<News> newsList = newsRepository.findByNews(newsTitle);
        for (News news : newsList) {
            newsRepository.delete(news);
        }
        sendTextMessage(chatId, "Новини з заголовком '" + newsTitle + "' видалено.");
    }

    private void deleteAchievementByTitle(Long chatId, String achievementTitle) {
        Iterable<Achievements> achievementsList = achievementRepository.findByAchievements(achievementTitle);
        for (Achievements achievement : achievementsList) {
            achievementRepository.delete(achievement);
        }
        sendTextMessage(chatId, "Досягнення з заголовком '" + achievementTitle + "' видалено.");
    }

    void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Apozh_bot";
    }

    @Override
    public String getBotToken() {
        return "6527308111:AAF8jsN2KTJTQ6wvWZPLUCFeNhAEFK1P_AU";
    }
}