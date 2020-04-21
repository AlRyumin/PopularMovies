package com.example.popularmoviesapp.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.popularmoviesapp.model.Trailer;

import java.util.List;

@Dao
public interface TrailerDao {
    @Query("SELECT * FROM trailer WHERE movieId = :movieId")
    List<Trailer> get(Long movieId);
}
