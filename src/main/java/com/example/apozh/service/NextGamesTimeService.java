package com.example.apozh.service;

import com.example.apozh.Repository.NextGamesTimeRepository;
import com.example.apozh.entity.NextGamesTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class NextGamesTimeService {
    @Autowired
    private NextGamesTimeRepository nextGamesTimeRepository;
    private final String URL = "https://ksl.co.ua/tournament/1024666/calendar?round_id=1043340&type=dates";

    public void fetchAndSaveSchedule() {
        RestTemplate restTemplate = new RestTemplate();
        String html = restTemplate.getForObject(URL, String.class);

        Document doc = Jsoup.parse(html);
        Elements teamNameElements = doc.select("span.schedule__team-name:containsOwn(FC APOZH)");

        Map<String, Month> monthMap = new HashMap<>();
        monthMap.put("січня", Month.JANUARY);
        monthMap.put("лютого", Month.FEBRUARY);
        monthMap.put("березня", Month.MARCH);
        monthMap.put("квітня", Month.APRIL);
        monthMap.put("травня", Month.MAY);
        monthMap.put("червня", Month.JUNE);
        monthMap.put("липня", Month.JULY);
        monthMap.put("серпня", Month.AUGUST);
        monthMap.put("вересня", Month.SEPTEMBER);
        monthMap.put("жовтня", Month.OCTOBER);
        monthMap.put("листопада", Month.NOVEMBER);
        monthMap.put("грудня", Month.DECEMBER);

        Map<String, DayOfWeek> dayOfWeekMap = new HashMap<>();
        dayOfWeekMap.put("понеділок", DayOfWeek.MONDAY);
        dayOfWeekMap.put("вівторок", DayOfWeek.TUESDAY);
        dayOfWeekMap.put("середа", DayOfWeek.WEDNESDAY);
        dayOfWeekMap.put("четвер", DayOfWeek.THURSDAY);
        dayOfWeekMap.put("п'ятниця", DayOfWeek.FRIDAY);
        dayOfWeekMap.put("субота", DayOfWeek.SATURDAY);
        dayOfWeekMap.put("неділя", DayOfWeek.SUNDAY);

        for (Element teamNameElement : teamNameElements) {
            Element dateElement = teamNameElement.parents().select(".schedule__head-text").first();
            Element timeElement = teamNameElement.parents().select(".schedule__time").first();
            Element placeElement = teamNameElement.parents().select(".schedule__place").first();

            if (dateElement != null && timeElement != null && placeElement != null) {
                String dateText = dateElement.text().trim();
                String[] dateParts = dateText.split(" ");
                if (dateParts.length == 3) {
                    int day = Integer.parseInt(dateParts[0]);
                    String monthText = dateParts[1].toLowerCase().replaceAll("\\p{Punct}|\\s", "");
                    String dayOfWeekText = dateParts[2].toLowerCase();
                    Month month = monthMap.get(monthText);
                    DayOfWeek dayOfWeek = dayOfWeekMap.get(dayOfWeekText);

                    if (month != null && dayOfWeek != null) {
                        int currentYear = LocalDate.now().getYear();
                        LocalDate date = LocalDate.of(currentYear, month, day);
                        date = date.with(TemporalAdjusters.nextOrSame(dayOfWeek));

                        String timeText = timeElement.text().trim();
                        LocalTime time = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("H:mm"));
                        String placeText = placeElement.text().trim();

                        NextGamesTime nextGame = new NextGamesTime();
                        nextGame.setDate(date);
                        nextGame.setTime(time);
                        nextGame.setLocation(placeText);

                        nextGamesTimeRepository.save(nextGame);
                    } else {
                    }
                } else {
                }

            } else {
                System.out.println("Информация о дате, времени и месте не найдена для команды FC APOZH.");
            }
        }
    }
}
