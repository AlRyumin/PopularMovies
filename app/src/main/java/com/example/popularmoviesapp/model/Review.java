package com.example.popularmoviesapp.model;

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;
    private final int contentLength = 100;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getShortContent() {
        if(!isLongContent()){
            return content;
        }

        String substring = content.substring(0, Math.min(content.length(), contentLength));
        substring += "...";

        return substring;
    }

    public boolean isLongContent(){
        if(content.length() > contentLength){
            return true;
        }

        return false;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
