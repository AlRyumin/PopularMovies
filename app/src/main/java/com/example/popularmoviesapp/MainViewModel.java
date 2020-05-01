package com.example.popularmoviesapp;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popularmoviesapp.data.Constant;
import com.example.popularmoviesapp.database.AppDatabase;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utilities.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Movie>> movies;
    int sortType;

    public MainViewModel(Application application) {
        super(application);
        movies = new MutableLiveData<>();
    }

    private void loadMovies() {
        if(sortType == Constant.SORT_TYPE_FAVORITE_MOVIES){
            loadMoviesFromDataBase();
        } else {
            loadMoviesFromApi();
        }
    }

    private void loadMoviesFromDataBase(){
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Thread thread = new Thread(){
            @Override
            public void run() {
                List<Movie> dBMovies = database.movieDao().get();
                if(dBMovies.isEmpty()){
                    movies.postValue(new ArrayList<>());
                } else {
                    movies.postValue((ArrayList<Movie>) dBMovies);
                }
            }
        };
        thread.start();
    }

    private void loadMoviesFromApi(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    ArrayList<Movie> movieList = JsonUtils.getMovieList(sortType);
                    movies.postValue(movieList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        if (movies.getValue() == null) {
            loadMovies();
        }
        return movies;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
        loadMovies();
    }
}
