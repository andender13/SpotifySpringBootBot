package com.example.springbootmusictgbot.spotify_api.response_models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    @JsonProperty("album")
    private Album album;
    @JsonProperty("artists")
    private List<Artist> artists;
    @JsonProperty("available_markets")
    private List<String> availableMarkets;
    @JsonProperty("disc_number")
    private int discNumber;
    @JsonProperty("duration_ms")
    private int durationMs;
    @JsonProperty("explicit")
    private boolean explicit;
    @JsonProperty("external_ids")
    private ExternalIds externalIds;
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    @JsonProperty("href")
    private String href;
    @JsonProperty("id")
    private String id;
    @JsonProperty("is_playable")
    private boolean isPlayable;
    @JsonProperty("linked_from")
    private Object linkedFrom;
    @JsonProperty("restrictions")
    private Restrictions restrictions;
    @JsonProperty("name")
    private String name;
    @JsonProperty("popularity")
    private int popularity;
    @JsonProperty("preview_url")
    private String previewUrl;
    @JsonProperty("track_number")
    private int trackNumber;
    @JsonProperty("type")
    private String type;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("is_local")
    private boolean isLocal;
}
