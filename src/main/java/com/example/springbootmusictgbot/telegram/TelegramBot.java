package com.example.springbootmusictgbot;

import com.example.springbootmusictgbot.Service.UserService;
import com.example.springbootmusictgbot.database.entity.Role;
import com.example.springbootmusictgbot.database.entity.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.DefaultBotOptions;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    @Autowired
    private final UserService userService = new UserService();

    public TelegramBot(DefaultBotOptions options, String botToken, String botUsername) {
        super(options);
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getUserName();
            switch (messageText) {
                case "/start": {
                    sendStartMessage(userName, chatId);
                    User user = new User(null,userName ,Role.USER);
                    userService.saveUser(user);
                    break;
                }
                case "/help": {
                    break;
                }
                default: {
                    sendMessage("Данная команда не поддерживается! Попробуйте написать /help для дополнительной информации.", chatId);
                    break;
                }
            }


        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }


    void sendStartMessage(String name, long chatId) throws TelegramApiException {
        sendMessage("Привет " + name + ", добро пожаловать!", chatId);
    }

    private void sendMessage(String text, long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        execute(message);
    }

}
