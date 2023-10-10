package com.example.apozh.controllers;

import com.example.apozh.entity.Statistics;
import com.example.apozh.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/apozh")
    @ResponseBody
    public ResponseEntity<Statistics> getApozhStatistics() {
        Statistics apoStatistics = statisticsService.getApozhStatistics();
        return ResponseEntity.ok(apoStatistics);
    }
}
