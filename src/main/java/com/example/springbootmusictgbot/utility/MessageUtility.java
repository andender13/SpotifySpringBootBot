package com.example.springbootmusictgbot.utility;

import com.example.springbootmusictgbot.database.entity.User;
import com.example.springbootmusictgbot.spotify_api.response_models.Artist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtility {
    private static final MessageUtility messageUtility = new MessageUtility();

    private MessageUtility() {
    }

    static public MessageUtility getInstance() {
        return messageUtility;
    }

    public String getAllUsersString(List<User> users) {
        StringBuilder usersString = new StringBuilder();
        for (User user : users) {
            usersString.append("ID: ").append(user.getId()).append("\n  ").
                    append("Имя: ").append(user.getName()).append("\n  ").
                    append("Роль: ").append(user.getRole()).append("\n  ").
                    append("ChatId: ").append(user.getChatId()).append("\n");
        }
        return usersString.toString();
    }

    public String getAllCommandsString(HashMap<String, String> commandMap) {
        StringBuilder commandsString = new StringBuilder();
        commandsString.append("Список доступных комманд:").append("\n\n");
        for (Map.Entry<String, String> stringStringEntry : commandMap.entrySet()) {
            commandsString.append(stringStringEntry.getKey()).append(": ").
                    append(stringStringEntry.getValue()).append("\n\n");
        }
        return commandsString.toString();
    }

    public String prepareArtistCaption(Artist artist) {
        StringBuilder artistCaption = new StringBuilder(artist.getName()).append("\nЖанры: ");
        for (int i = 0; i < artist.getGenres().size(); i++) {
            artistCaption.append(artist.getGenres().get(i));
            if (i != artist.getGenres().size() - 1) {
                artistCaption.append(", ");
            }
        }
        artistCaption.append("\n")
                .append("Количество подпищиков: ")
                .append(artist.getFollowers().getAmount())
                .append("\n");
        artistCaption.append("Популярность в Spotify: ")
                .append(artist.getPopularity()).append("/100");
        return artistCaption.toString();
    }
}
