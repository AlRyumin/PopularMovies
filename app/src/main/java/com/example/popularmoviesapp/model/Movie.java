package com.example.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie implements Parcelable {
    @PrimaryKey
    private final Long id;
    private final String title;
    private final String originalTitle;
    private final String poster;
    private final String backdrop;
    private final String synopsis;
    private final String releaseDate;
    private final Double voteAverage;

    public Movie(Long id, String title, String originalTitle, String poster, String backdrop, String synopsis, String releaseDate, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.poster = poster;
        this.backdrop = backdrop;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    private Movie(Parcel parcel) {
        id = parcel.readLong();
        title = parcel.readString();
        originalTitle = parcel.readString();
        poster = parcel.readString();
        backdrop = parcel.readString();
        synopsis = parcel.readString();
        releaseDate = parcel.readString();
        voteAverage = parcel.readDouble();
    }

    public Long getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
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
        return releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeString(synopsis);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
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
