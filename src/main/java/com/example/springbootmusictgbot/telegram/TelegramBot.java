package com.example.springbootmusictgbot.telegram;

import com.example.springbootmusictgbot.database.entity.User;
import com.example.springbootmusictgbot.enums.BotState;
import com.example.springbootmusictgbot.enums.Role;
import com.example.springbootmusictgbot.service.UserService;
import com.example.springbootmusictgbot.spotify_api.RestRequestOperations;
import com.example.springbootmusictgbot.spotify_api.SpotifyApi;
import com.example.springbootmusictgbot.spotify_api.response_models.Artist;
import com.example.springbootmusictgbot.spotify_api.response_models.Track;
import com.example.springbootmusictgbot.utility.JsonUtility;
import com.example.springbootmusictgbot.utility.MessageUtility;
import com.example.springbootmusictgbot.utility.StorageUtility;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TelegramBot extends TelegramLongPollingBot {

    private static final String EMPTY_MESSAGE = "";

    private final String botToken;
    private final String botUsername;
    private final UserService userService;
    private final SpotifyApi spotifyApi;
    private final RestRequestOperations restRequestOperations;
    private static final HashMap<String, String> botCommands = getBotCommands();
    private final Map<Long, BotState> userStates = new HashMap<>();


    public TelegramBot(DefaultBotOptions options, String botToken, String botUsername, SpotifyApi spotifyApi, UserService userService, RestRequestOperations restRequestOperations) {
        super(options);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.spotifyApi = spotifyApi;
        this.userService = userService;
        this.restRequestOperations = restRequestOperations;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getUserName();

            if (!userStates.containsKey(chatId)) {
                userStates.put(chatId, BotState.START);
            }
            if (userStates.get(chatId) == BotState.START) {
                handleUserStatus(chatId, messageText, userName);
                System.out.println(userStates.get(chatId));
            } else {
                handleUserMultipleCommand(chatId, messageText, userName);
            }
        }
    }


    private void handleUserMultipleCommand(long chatId, String messageText, String userName) throws TelegramApiException {
        BotState state = userStates.get(chatId);
        switch (state) {
            case WAITING_FOR_ARTIST_NAME:
                Artist artist = spotifyApi.artistSearchRequest(messageText);
                restRequestOperations.getPhotoByURL(artist.getImages().getFirst().getUrl(), artist.getName());
                System.out.println(MessageUtility.getInstance().prepareArtistCaption(artist));
                sendPhoto(artist.getName(), chatId, artist.getExternalUrls().getSpotify(), MessageUtility.getInstance().prepareArtistCaption(artist));
                Track track = JsonUtility.getInstance().parseTracksJSON(spotifyApi.artistTopTracksRequest(artist.getId())).getFirst();
                restRequestOperations.getMp3ByURL(track.getPreviewUrl(), artist.getName() + " - " + track.getName());
                sendMP3(artist.getName() + " - " + track.getName(), chatId, track.getExternalUrls().getSpotify());
                userStates.put(chatId, BotState.START);
                break;
            case WAITING_FOR_TRACK_NAME:
                Track track1 = JsonUtility.getInstance().parseTrackJSON(spotifyApi.trackSearchRequest(messageText));
                System.out.println(track1);
                restRequestOperations.getPhotoByURL(track1.getAlbum().getImages().getFirst().getUrl(), userName);
                sendPhoto(userName, chatId, track1.getArtists().getFirst().getExternalUrls().getSpotify(), EMPTY_MESSAGE);
                restRequestOperations.getMp3ByURL(track1.getPreviewUrl(), track1.getArtists().getFirst().getName() + " - " + track1.getName());
                userStates.put(chatId, BotState.START);
                sendMP3(track1.getArtists().getFirst().getName() + " - " + track1.getName(), chatId, track1.getExternalUrls().getSpotify());
                break;
        }
    }

    private void registerUser(String userName, long chatId) {
        if (userService.findUserByChatId(chatId) == null) {
            User user = new User(null, userName, Role.USER, chatId);
            userService.saveUser(user);
        }
    }


    public void handleUserStatus(Long chatId, String messageText, String userName) throws TelegramApiException {
        switch (messageText.trim()) {
            case "/find_artist": {
                sendMessage("Введите псевдоним артиста для поиска:", chatId);
                userStates.put(chatId, BotState.WAITING_FOR_ARTIST_NAME);
                return;
            }
            case "/find_track": {
                sendMessage("Введите назвние песни для поиска:", chatId);
                userStates.put(chatId, BotState.WAITING_FOR_TRACK_NAME);
                return;
            }
            case "/start": {
                if (!userStates.containsKey(chatId)) {
                    userStates.put(chatId, BotState.START);
                }
                registerUser(userName, chatId);
                sendStartMessage(userName, chatId);
                return;
            }
            case "/help": {
                String commandsString = MessageUtility.getInstance().getAllCommandsString(botCommands);
                sendMessage(commandsString, chatId);
                return;
            }
            case "/showUsers": {
                if (userService.findUserByChatId(chatId).getRole() == Role.ADMIN) {
                    String usersString = MessageUtility.getInstance().getAllUsersString(userService.getAllUsers());
                    sendMessage(usersString, chatId);
                    return;
                }
            }
            default:
                sendMessage("Данная команда не поддерживается! Попробуйте написать /help для дополнительной информации.", chatId);
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

    public static HashMap<String, String> getBotCommands() {
        HashMap<String, String> botCommands = new HashMap<>();
        botCommands.put("/start", "Активация бота");
        botCommands.put("/help", "Инструкция пользования ботом");
        botCommands.put("/find_artist", "Поиск информации об артисте");
        botCommands.put("/find_track", "Поиск информации о песне");
        return botCommands;
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

    public void registerCommands() {
        List<BotCommand> commands = new ArrayList<>();
        for (Map.Entry<String, String> commandEntry : botCommands.entrySet()) {
            String command = commandEntry.getKey();
            String description = commandEntry.getValue();

            if (!command.matches("^/[a-z0-9_]+$")) {
                System.err.println("Invalid command: " + command);
                continue;
            }
            if (description.length() > 256) {
                System.err.println("Description too long for command: " + command);
                continue;
            }
            commands.add(new BotCommand(command, description));
        }
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(String title, long chatId, String URL, String caption) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        String filePath = "src/main/resources/" + title + ".jpg";
        sendPhoto.setPhoto(new InputFile(new File(filePath)));
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup("Профиль артиста в Spotify", URL);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendPhoto);
        StorageUtility.getInstance().deleteJPGFile(filePath);
    }

    public void sendMP3(String title, long chatId, String trackURL) throws TelegramApiException {
        SendAudio sendAudio = new SendAudio();
        String filePath = "src/main/resources/" + title + ".mp3";
        sendAudio.setChatId(chatId);
        sendAudio.setAudio(new InputFile(new File(filePath)));
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup("Полная версия трека в Spotify", trackURL);

        sendAudio.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendAudio);
        StorageUtility.getInstance().deleteMp3File(filePath);
    }

    private static @NotNull InlineKeyboardMarkup getInlineKeyboardMarkup(String title, String URL) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(title);
        button.setUrl(URL);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public void onRegister() {
        super.onRegister();
        registerCommands();
    }
}
