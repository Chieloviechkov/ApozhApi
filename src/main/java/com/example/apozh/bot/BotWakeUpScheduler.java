package com.example.apozh.bot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BotWakeUpScheduler {
    private final MyTelegramBot myTelegramBot; // Ваш бот

    public BotWakeUpScheduler(MyTelegramBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }

    // Этот метод будет выполняться каждые 3 часа
    @Scheduled(fixedRate = 30 * 60 * 1000) // 3 часа в миллисекундах
    public void wakeUpBot() {
        // Отправьте пустое сообщение боту, чтобы "разбудить" его
        // Вы можете использовать любую команду или текст
        long chatId = 123456789; // Замените на реальный chatId вашего бота
        myTelegramBot.sendTextMessage(chatId, "/wake_up"); // Отправить команду /wake_up боту
    }
}
