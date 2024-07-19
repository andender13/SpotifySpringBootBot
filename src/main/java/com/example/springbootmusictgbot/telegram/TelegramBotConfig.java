package com.example.springbootmusictgbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class TelegramBotConfig {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Bean
    public TelegramBot telegramBot() {
        var botOptions = new DefaultBotOptions();
        return new TelegramBot(botOptions, botToken, botName);
    }
}
