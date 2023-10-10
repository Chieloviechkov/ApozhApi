package com.example.apozh.service;

import com.example.apozh.Repository.StatisticsRepository;
import com.example.apozh.entity.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public Statistics getApozhStatistics() {
        Optional<Statistics> apoStatisticsOptional = statisticsRepository.findById(1L);
        return apoStatisticsOptional.orElseGet(Statistics::new);
    }

    public void saveStatistics(Statistics statistics) {
        statisticsRepository.save(statistics);
    }

}
