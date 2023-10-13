package com.example.apozh.bot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BotWakeUpScheduler {
    private final MyTelegramBot myTelegramBot;

    public BotWakeUpScheduler(MyTelegramBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void wakeUpBot() {
        long chatId = 123456789;
        myTelegramBot.sendTextMessage(chatId, "/wake_up");
    }
}
