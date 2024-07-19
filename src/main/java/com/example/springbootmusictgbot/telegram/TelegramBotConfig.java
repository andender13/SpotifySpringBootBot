package com.example.springbootmusictgbot.telegram;

import com.example.springbootmusictgbot.service.UserService;
import com.example.springbootmusictgbot.spotify_api.RestRequestOperations;
import com.example.springbootmusictgbot.spotify_api.SpotifyApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

    @Value("${telegramBot.token}")
    private String botToken;

    @Value("${telegramBot.name}")
    private String botUsername;

    @Bean
    public TelegramBot telegramBot(UserService userService, SpotifyApi spotifyApi, RestRequestOperations restRequestOperations) {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        TelegramBot telegramBot = new TelegramBot(botOptions, botToken, botUsername, spotifyApi,userService,restRequestOperations);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return telegramBot;
    }
}
