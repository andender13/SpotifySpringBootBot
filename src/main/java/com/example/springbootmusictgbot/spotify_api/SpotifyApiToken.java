package com.example.springbootmusictgbot.spotify_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyApiConfig {
    private String accessToken;
    private String tokenType;
    private int expiresIn;
}
