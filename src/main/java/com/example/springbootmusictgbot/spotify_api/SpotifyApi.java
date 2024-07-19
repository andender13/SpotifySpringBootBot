package com.example.springbootmusictgbot.spotify_api;

import com.example.springbootmusictgbot.spotify_api.response_models.Artist;
import com.example.springbootmusictgbot.utility.JsonUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
public class SpotifyApi {

    @Value("${client.Id}")
    private String clientId;

    @Value("${client.Secret}")
    private String clientSecret;

    private final WebClient webClient;
    private SpotifyApiToken spotifyToken;

    @Autowired
    public SpotifyApi(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Scheduled(fixedRate = 3600000)
    private synchronized void setupSpotifyToken() {
        if (spotifyToken == null) {
            String credentials = clientId + ":" + clientSecret;
            String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            String responseBody = this.webClient.post()
                    .uri("https://accounts.spotify.com/api/token")
                    .header("Authorization", "Basic " + base64Credentials)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (responseBody != null) {
                JSONObject jsonResponse = new JSONObject(responseBody);
                spotifyToken = new SpotifyApiToken(
                        jsonResponse.getString("access_token"),
                        jsonResponse.getString("token_type"),
                        jsonResponse.getInt("expires_in")
                );
            } else {
                System.out.println("Error: Failed to get response");
            }
        }
    }

    public Artist artistSearchRequest(String artistName) {
        String responseBody = this.webClient.get()
                .uri("https://api.spotify.com/v1/search?q=" + artistName + "&type=artist&limit=1")
                .header("Authorization", "Bearer " + spotifyToken.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (responseBody != null) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return JsonUtility.getInstance().parseArtistJSON(jsonResponse);
        }
        System.out.println("Error: Failed to get response");
        return null;
    }

    public String artistAlbumsRequest(String artistId) {
        String responseBody = this.webClient.get()
                .uri("https://api.spotify.com/v1/artists/" + artistId + "/albums")
                .header("Authorization", "Bearer " + spotifyToken.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (responseBody != null) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            System.out.println(jsonResponse.toString());
            return jsonResponse.toString();
        }
        System.out.println("Error: Failed to get response");
        return null;
    }

    public String meRequest(String artistId) {
        String responseBody = this.webClient.get()
                .uri("https://api.spotify.com/v1/me")
                .header("Authorization", "Bearer " + spotifyToken.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (responseBody != null) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.toString();
        }
        System.out.println("Error: Failed to get response");
        return null;
    }

    public JSONObject artistTopTracksRequest(String artistId) {
        String responseBody = this.webClient.get()
                .uri("https://api.spotify.com/v1/artists/" + artistId + "/top-tracks")
                .header("Authorization", "Bearer " + spotifyToken.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (responseBody != null) {
            return new JSONObject(responseBody);
        }
        System.out.println("Error: Failed to get response");
        return null;
    }

    public JSONObject trackSearchRequest(String trackName) {
        String responseBody = this.webClient.get()
                .uri("https://api.spotify.com/v1/search?q=" + trackName + "&type=track&limit=1")
                .header("Authorization", "Bearer " + spotifyToken.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (responseBody != null) {
            return new JSONObject(responseBody);
        }
        System.out.println("Error: Failed to get response");
        return null;
    }
}
