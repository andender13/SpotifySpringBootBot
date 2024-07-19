package com.example.springbootmusictgbot.utility;

import com.example.springbootmusictgbot.spotify_api.response_models.Artist;
import com.example.springbootmusictgbot.spotify_api.response_models.Track;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class JsonUtility {
    private static final JsonUtility jsonUtility = new JsonUtility();

    private JsonUtility() {
    }

    public static JsonUtility getInstance() {
        return jsonUtility;
    }

    public Artist parseArtistJSON(JSONObject artistJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        Artist artist = null;
        try {
            // Считывание JSON файла
            JsonNode root = objectMapper.readTree(artistJson.toString());
            JsonNode itemsNode = root.path("artists").path("items");
            if (itemsNode.isMissingNode() || !itemsNode.isArray() || itemsNode.isEmpty()) {
                System.err.println("Items node is missing or empty in the JSON response.");
                return null;
            }
            // Предположим, что нас интересует первый элемент в массиве
            JsonNode artistNode = itemsNode.get(0);
            artist = objectMapper.readValue(artistNode.toString(), new TypeReference<Artist>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return artist;
    }


    public List<Track> parseTracksJSON(JSONObject trackJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Track> tracks = null;
        try {
            // Считывание JSON файла
            JsonNode root = objectMapper.readTree(String.valueOf(trackJson));
            JsonNode tracksNode = root.path("tracks");
            tracks = objectMapper.readValue(tracksNode.toString(), new TypeReference<>() {
            });

            // Доступ к данным
//            for (Track track : tracks) {
//                System.out.println("Track Name: " + track.getName());
//                System.out.println("Album Name: " + track.getAlbum().getName());
//                System.out.println("Album Type: " + track.getAlbum().getAlbumType());
//                System.out.println("Artist Name: " + track.getArtists().getFirst().getName());
//                System.out.println("Popularity: " + track.getPopularity());
//                System.out.println("Preview URL: " + track.getPreviewUrl());
//                if (!track.getAlbum().getImages().isEmpty()) {
//                    System.out.println("Album Image URL: " + track.getAlbum().getImages().getFirst().getUrl());
//                }
//                // Вы можете добавить больше логики для обработки данных
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tracks;
    }


    public Track parseTrackJSON(JSONObject trackJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        Track track = null;
        try {
            // Считывание JSON файла
            JsonNode root = objectMapper.readTree(trackJson.toString());
            JsonNode itemsNode = root.path("tracks").path("items");
            if (itemsNode.isMissingNode() || !itemsNode.isArray() || itemsNode.isEmpty()) {
                System.err.println("Items node is missing or empty in the JSON response.");
                return null;
            }

            JsonNode trackNode = itemsNode.get(0);
            track = objectMapper.readValue(trackNode.toString(), new TypeReference<Track>() {
            });

            } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        return track;
    }
}

