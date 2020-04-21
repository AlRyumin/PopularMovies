package com.example.popularmoviesapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Trailer {
    @PrimaryKey
    private String id;
    private String key;
    private String name;
    private String site;
    private String imageUrl;
    private String videoUrl;
    private Integer size;
    private String type;
    private long movieId;

    public Trailer(String id, String key, String name, String site, Integer size, String type, Long movieId) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.movieId = movieId;

        this.imageUrl = "https://img.youtube.com/vi/" + key + "/0.jpg";
        this.videoUrl = "https://www.youtube.com/watch?v=" + key;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public long getMovieId() { return movieId; }
}
