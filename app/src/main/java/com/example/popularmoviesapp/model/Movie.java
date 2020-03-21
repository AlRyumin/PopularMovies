package com.example.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie  implements Parcelable {
    private final Long id;
    private final String title;
    private final String original_title;
    private final String poster;
    private final String backdrop;
    private final String synopsis;
    private final String release_date;
    private final Double vote_average;

    public Movie(Long id, String title, String original_title, String poster, String backdrop, String synopsis, String release_date, Double vote_average) {
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.poster = poster;
        this.backdrop = backdrop;
        this.synopsis = synopsis;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    private Movie(Parcel parcel) {
        id = parcel.readLong();
        title = parcel.readString();
        original_title = parcel.readString();
        poster = parcel.readString();
        backdrop = parcel.readString();
        synopsis = parcel.readString();
        release_date = parcel.readString();
        vote_average = parcel.readDouble();
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getPoster() {
        return poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public Double getVote_average() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(original_title);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeString(synopsis);
        parcel.writeString(release_date);
        parcel.writeDouble(vote_average);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
