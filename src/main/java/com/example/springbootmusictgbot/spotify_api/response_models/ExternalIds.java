package com.example.springbootmusictgbot.spotify_api.response_models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public  class ExternalIds {
    @JsonProperty("isrc")
    private String isrc;
    @JsonProperty("ean")
    private String ean;
    @JsonProperty("upc")
    private String upc;

    // Getters and setters
}
