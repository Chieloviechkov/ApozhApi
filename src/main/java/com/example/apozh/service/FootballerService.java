package com.example.apozh.service;

import com.example.apozh.Repository.FootballerRepository;
import com.example.apozh.entity.Footballer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class FootballerService {
private final FootballerRepository footballerRepository;
    @Autowired
    public FootballerService(FootballerRepository footballerRepository) {

        this.footballerRepository = footballerRepository;
    }

    public static final Map<String, List<Number>> playerStatisticsMap = new HashMap<>();

    {
        playerStatisticsMap.put("Олександр Алімов", Arrays.asList(3, 4, 7, 2, 0, 0, null)); //Номер, игры, голы, голевые передачи, желтые, красные
        playerStatisticsMap.put("Максим Білий", Arrays.asList(5, 23, 3, 1, 0, 0, null));
        playerStatisticsMap.put("Іван Городній", Arrays.asList(99, 2, 1, 3, 0, 0, null));
        playerStatisticsMap.put("Кирило Єфімов", Arrays.asList(97, 2, 2, 3, 0, 0, null));
        playerStatisticsMap.put("Антон Тєрєшков", Arrays.asList(96, 1, 1, 1, 0, 0, null));
        playerStatisticsMap.put("Іван Селюк", Arrays.asList(89, 1, 0, 0, 0, 0, null));
        playerStatisticsMap.put("Роман Романко", Arrays.asList(12, 37, 7, 2, 1, 0, null));
        playerStatisticsMap.put("Ігор Воскривенко", Arrays.asList(7, 18, 9, 4, 0, 0, null));
        playerStatisticsMap.put("Валентин Солодкий", Arrays.asList(15, 24, 9, 11, 7, 0, null));
        playerStatisticsMap.put("Іван Дума", Arrays.asList(11, 42, 9, 8, 3, 0, null));
        playerStatisticsMap.put("Костянтин Жигалюк", Arrays.asList(70, 27, 35, 14, 3, 0, null));
        playerStatisticsMap.put("Денис Завалішин", Arrays.asList(23, 38, 9, 14, 3, 1, null));
        playerStatisticsMap.put("Михайло Ковальчук", Arrays.asList(81, 91, 114, 48, 14, 1, null));
        playerStatisticsMap.put("Дмитро Ковальчук", Arrays.asList(77, 10, 2, 3, 2, 0, null));
        playerStatisticsMap.put("Дмитро Котов", Arrays.asList(4, 6, 0, 1, 1, 0, null));
        playerStatisticsMap.put("Максим Котов", Arrays.asList(9, 7, 1, 1, 0, 0, null));
        playerStatisticsMap.put("Олексій Нігрей", Arrays.asList(18, 32, 16, 10, 2, 0, null));
        playerStatisticsMap.put("Володимир Передерій", Arrays.asList(69, 18, 6, 9, 1, 0, null));
        playerStatisticsMap.put("Євген Розстальний", Arrays.asList(2, 20, 2, 2, 2, 0, null));
        playerStatisticsMap.put("Дмитро Роспутько", Arrays.asList(10, 54, 26, 20, 1, 0, null));
        playerStatisticsMap.put("Іван Савченко", Arrays.asList(22, 83, 8, 16, 5, 2, null));
        playerStatisticsMap.put("Руслан Світлий", Arrays.asList(8, 81, 69, 54, 7, 0, null));
        playerStatisticsMap.put("Антон Кирык", Arrays.asList(1, 3, -3, 0, 0, 0, 0.0));
        playerStatisticsMap.put("Віктор Грабар", Arrays.asList(99, 11, -52, 1, 1, 0, 4.7));
        playerStatisticsMap.put("Сергій Чаплинський", Arrays.asList(98, 10, -27, 1, 0, 0, 2.7));
        playerStatisticsMap.put("Олексій Лисенко", Arrays.asList(66, 6, 1, 1, 2, 0, null));
        playerStatisticsMap.put("Максим Чєловєчков", Arrays.asList(71, 57, -163, 3, 1, 0, 0.0));
        playerStatisticsMap.put("Ілля Ярошенко", Arrays.asList(46, 1, 0, 0, 0, 0, null));
        playerStatisticsMap.put("Дмитро Безпалько", Arrays.asList(36, 1, 0, 0, 0, 0, null));
        playerStatisticsMap.put("Пʼяткін Ерік", Arrays.asList(26, 1, 1, 0, 0, 0, null));
        playerStatisticsMap.put("Коваленко Рафаель", Arrays.asList(27, 1, 0, 0, 0, 0, null));
    }

    public void updateMissedGoalsPerGame() {
        Locale.setDefault(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00");
        playerStatisticsMap.forEach((player, stats) -> {
            int missedGoals = (int) stats.get(2);
            int gamesPlayed = (int) stats.get(1);
            if (missedGoals < 0 && gamesPlayed != 0) {
                double missedGoalsPerGame = (double) missedGoals / gamesPlayed;
                String formattedMissedGoalsPerGame = df.format(missedGoalsPerGame);
                stats.set(6, Double.parseDouble(formattedMissedGoalsPerGame));
            } else {
                stats.set(6, null);
            }
        });
    }

    public static final Map<Character, List<String>> playerStatisticsMapByInitial = new HashMap<>();

    {
        playerStatisticsMap.forEach((player, stats) -> {
            char initial = Character.toUpperCase(player.charAt(0));
            if (!playerStatisticsMapByInitial.containsKey(initial)) {
                playerStatisticsMapByInitial.put(initial, new ArrayList<>());
            }
            playerStatisticsMapByInitial.get(initial).add(player);
        });
    }

    public void updateMatchStatistics(String playerNames) {
        String[] playerNamesArray = playerNames.split("[,;]");

        for (String playerName : playerNamesArray) {
            playerName = playerName.trim();

            if (playerStatisticsMap.containsKey(playerName)) {
                List<Number> playerStats = playerStatisticsMap.get(playerName);

                int gamesPlayed = (int) playerStats.get(1);
                playerStats.set(1, gamesPlayed + 1);

                playerStatisticsMap.put(playerName, playerStats);

                char initial = Character.toUpperCase(playerName.charAt(0));
                List<String> playersByInitial = playerStatisticsMapByInitial.get(initial);
                playersByInitial.remove(playerName);
                playersByInitial.add(playerName);
                playerStatisticsMapByInitial.put(initial, playersByInitial);

                System.out.println("Статистика для футболиста " + playerName + " успешно обновлена. Количество сыгранных матчей увеличено на 1.");
            } else {
                System.out.println("Футболист с именем " + playerName + " не найден в статистике. Обновление не выполнено.");
            }
        }
    }

    public void updatePlayerStatistics(String playerName, int goalsScored, int assists, int yellowCards, int redCards) {
        String[] parts = playerName.split(" ");
        String lastName = parts[1];
        char initial = Character.toUpperCase(parts[0].charAt(0));

        for (String player : playerStatisticsMap.keySet()) {
            if (player.endsWith(lastName) && player.startsWith(String.valueOf(initial))) {
                List<Number> playerStats = playerStatisticsMap.get(player);
                int currentGoals = (int) playerStats.get(2);
                int currentAssists = (int) playerStats.get(3);
                int currentYellowCards = (int) playerStats.get(4);
                int currentRedCards = (int) playerStats.get(5);

                playerStats.set(2, currentGoals + goalsScored);
                playerStats.set(3, currentAssists + assists);
                playerStats.set(4, currentYellowCards + yellowCards);
                playerStats.set(5, currentRedCards + redCards);
                savePlayerStatisticsToDatabase(player, playerStats);
            }
        }
        updateMissedGoalsPerGame();
    }
    public void updatePlayerStatisticsWithGames(String playerName,int matches, int goalsScored, int assists, int yellowCards, int redCards) {
        String[] parts = playerName.split(" ");
        String lastName = parts[1];
        char initial = Character.toUpperCase(parts[0].charAt(0));

        for (String player : playerStatisticsMap.keySet()) {
            if (player.endsWith(lastName) && player.startsWith(String.valueOf(initial))) {
                List<Number> playerStats = playerStatisticsMap.get(player);

                playerStats.set(1, matches);
                playerStats.set(2, goalsScored);
                playerStats.set(3, assists);
                playerStats.set(4, yellowCards);
                playerStats.set(5, redCards);
                savePlayerStatisticsToDatabase(player, playerStats);
            }
        }
        updateMissedGoalsPerGame();
    }

    private void savePlayerStatisticsToDatabase(String playerName, List<Number> playerStats) {
        String[] parts = playerName.split(" ");
        String lastName = parts[1];
        String firstName = parts[0];
        System.out.println(lastName);
        System.out.println(firstName);
        Footballer footballer = footballerRepository.findByLastNameAndFirstName(lastName, firstName);
        if (footballer != null) {
            footballer.setGoals(playerStats.get(2).intValue());
            footballer.setAssists(playerStats.get(3).intValue());
            footballer.setYellowCards(playerStats.get(4).intValue());
            footballer.setRedCards(playerStats.get(5).intValue());

            footballerRepository.save(footballer);
            System.out.println("Статистика для футболиста " + playerName + " успешно сохранена в базе данных.");
        } else {
            System.out.println("Футболист с именем " + playerName + " не найден в базе данных. Сохранение не выполнено.");
        }
    }


    public void deletePlayerStatistics(String playerName, int matches, int goalsScored, int assists, int yellowCards, int redCards) {
        String[] parts = playerName.split(" ");
        String lastName = parts[1];
        char initial = Character.toUpperCase(parts[0].charAt(0));

        for (String player : playerStatisticsMap.keySet()) {
            if (player.endsWith(lastName) && player.startsWith(String.valueOf(initial))) {
                List<Number> playerStats = playerStatisticsMap.get(player);
                playerStats.set(1, 0);
                playerStats.set(2, 0);
                playerStats.set(3, 0);
                playerStats.set(4, 0);
                playerStats.set(5, 0);
            }
        }
        updateMissedGoalsPerGame();
    }

    public List<Footballer> scrapeFootballers(String url) {
        List<Footballer> footballersList = new ArrayList<>();
        boolean stopParsing = false;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements footballerItems = doc.select(".composition-list__item");

            for (Element footballerItem : footballerItems) {
                String photoUrl = footballerItem.select(".composition-list__player-photo img").attr("src");
                String firstName = footballerItem.select(".composition-list__player-first-name").last().text().trim();
                String lastName = footballerItem.select(".composition-list__player-last-name").last().text().trim();
                String amplua = footballerItem.select(".composition-list__player-amplua").last().text().trim();
                var birthDate = footballerItem.select(".composition-list__player-birth-date").text().split(",")[0].trim();

                String fullName = firstName + " " + lastName;
                Footballer existingFootballer = footballerRepository.findByLastNameAndFirstName(lastName, firstName);
                if (existingFootballer == null) {
                    Footballer footballer = new Footballer();
                    footballer.setImgUrl(photoUrl);
                    footballer.setFirstName(firstName);
                    footballer.setLastName(lastName);
                    footballer.setPosition(amplua);

                    List<Number> stats = playerStatisticsMap.get(fullName);
                    if (stats != null) {
                        int number = (int) stats.get(0);
                        int gamesPlayed = (int) stats.get(1);
                        int goals = (int) stats.get(2);
                        int assists = (int) stats.get(3);
                        int yellowCards = (int) stats.get(4);
                        int redCards = (int) stats.get(5);
                        Number missedGoalsPerGameNumber = stats.get(6);
                        double missedGoalsPerGame = (missedGoalsPerGameNumber != null) ? missedGoalsPerGameNumber.doubleValue() : 0.0;
                        footballer.setNumber(number);
                        footballer.setGames(gamesPlayed);
                        footballer.setGoals(goals);
                        footballer.setAssists(assists);
                        footballer.setYellowCards(yellowCards);
                        footballer.setRedCards(redCards);
                        footballer.setMissedGoals(missedGoalsPerGame);
                    }
                    try {
                        LocalDate dateOfBirth = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                        footballer.setDateOfBirth(dateOfBirth);
                    } catch (DateTimeParseException e) {
                        System.err.println("Ошибка парсинга даты для футболиста: " + fullName);
                    }
                    if (lastName.equals("Ахадов")) {
                        stopParsing = true;
                    }

                    if (stopParsing) {
                        break;
                    }
                    footballersList.add(footballer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        footballerRepository.saveAll(footballersList);
        return footballersList;
    }
}
