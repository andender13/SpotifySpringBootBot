package com.example.springbootmusictgbot.spotify_api.response_models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @JsonProperty("url")
    private String url;
    @JsonProperty("height")
    private int height;
    @JsonProperty("width")
    private int width;
}
