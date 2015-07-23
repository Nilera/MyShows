package me.myshows.android.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author Whiplash
 * @date 22.06.2015
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Show {

    private static final String IMAGE_URL = "http://media.myshows.me/shows/%s/%s/%s/%s";
    private static final String NORMAL = "normal";
    private static final String SMALL = "small";

    private final int id;
    private final String title;
    private final String ruTitle;
    private final String status;
    private final String country;
    private final String started;
    private final String ended;
    private final int year;
    private final int kinopoiskId;
    private final int tvrageId;
    private final int imdbId;
    private final int voted;
    private final float rating;
    private final int runtime;
    private final String image;
    private final int[] genres;
    private final Map<String, Episode> episodes;
    private final int watching;
    private final String[] images;
    private final String description;

    @JsonCreator
    public Show(@JsonProperty("id") int id, @JsonProperty("title") String title,
                @JsonProperty("ruTitle") String ruTitle, @JsonProperty("status") String status,
                @JsonProperty("country") String country, @JsonProperty("started") String started,
                @JsonProperty("ended") String ended, @JsonProperty("year") int year,
                @JsonProperty("kinopoiskId") int kinopoiskId, @JsonProperty("tvrageId") int tvrageId,
                @JsonProperty("imdbId") int imdbId, @JsonProperty("voted") int voted,
                @JsonProperty("rating") float rating, @JsonProperty("runtime") int runtime,
                @JsonProperty("image") String image, @JsonProperty("genres") int[] genres,
                @JsonProperty("episodes") Map<String, Episode> episodes, @JsonProperty("watching") int watching,
                @JsonProperty("images") String[] images, @JsonProperty("description") String description) {
        this.id = id;
        this.title = title;
        this.ruTitle = ruTitle;
        this.status = status;
        this.country = country;
        this.started = started;
        this.ended = ended;
        this.year = year;
        this.kinopoiskId = kinopoiskId;
        this.tvrageId = tvrageId;
        this.imdbId = imdbId;
        this.voted = voted;
        this.rating = rating;
        this.runtime = runtime;
        this.image = image;
        this.genres = genres;
        this.episodes = episodes;
        this.watching = watching;
        this.images = images;
        this.description = description;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("ruTitle")
    public String getRuTitle() {
        return ruTitle;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("started")
    public String getStarted() {
        return started;
    }

    @JsonProperty("ended")
    public String getEnded() {
        return ended;
    }

    @JsonProperty("year")
    public int getYear() {
        return year;
    }

    @JsonProperty("kinopoiskId")
    public int getKinopoiskId() {
        return kinopoiskId;
    }

    @JsonProperty("tvrageId")
    public int getTvrageId() {
        return tvrageId;
    }

    @JsonProperty("imdbId")
    public int getImdbId() {
        return imdbId;
    }

    @JsonProperty("voted")
    public int getVoted() {
        return voted;
    }

    @JsonProperty("rating")
    public float getRating() {
        return rating;
    }

    @JsonProperty("runtime")
    public int getRuntime() {
        return runtime;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("genres")
    public int[] getGenres() {
        return genres;
    }

    @JsonProperty("episodes")
    public Map<String, Episode> getEpisodes() {
        return episodes;
    }

    @JsonProperty("watching")
    public int getWatching() {
        return watching;
    }

    @JsonProperty("images")
    public String[] getImages() {
        return images;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public String getNormalImage() {
        return getImageUrl(NORMAL);
    }

    public String getSmallImage() {
        return getImageUrl(SMALL);
    }

    private String getImageUrl(String size) {
        if (images == null || images.length == 0) {
            return image;
        }
        String hash = images[0];
        return String.format(IMAGE_URL, size, hash.substring(0, 1), hash.substring(0, 2), hash);
    }
}
