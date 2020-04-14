package com.example.popularmoviesapp.model;

public class Trailer {
    private String id;
    private String key;
    private String name;
    private String site;
    private String image_url;
    private String video_url;
    private Integer size;
    private String type;

    public Trailer(String id, String key, String name, String site, Integer size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;

        this.image_url = "https://img.youtube.com/vi/" + key + "/0.jpg";
        this.video_url = "https://www.youtube.com/watch?v=" + key;
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
        return image_url;
    }

    public String getVideoUrl() {
        return video_url;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
