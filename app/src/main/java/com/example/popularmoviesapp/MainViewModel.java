package com.example.popularmoviesapp;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
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
    MutableLiveData<List<Movie>> movies;
    int sortType;
    int page;
    boolean isLastPage;

    public MainViewModel(Application application) {
        super(application);
        movies = new MutableLiveData<>();
        page = 1;
        isLastPage = false;
    }

    private void loadMovies() {
        if (sortType == Constant.SORT_TYPE_FAVORITE_MOVIES) {
            loadMoviesFromDataBase();
        } else {
            loadMoviesFromApi();
        }
    }

    private void loadMoviesFromDataBase() {
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Thread thread = new Thread() {
            @Override
            public void run() {
                List<Movie> dBMovies = database.movieDao().get();
                if (dBMovies.isEmpty()) {
                    movies.postValue(new ArrayList<>());
                } else {
                    movies.postValue((ArrayList<Movie>) dBMovies);
                }
                isLastPage = true;
            }
        };
        thread.start();
    }

    private void loadMoviesFromApi() {
        Thread thread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    List<Movie> movieList = JsonUtils.getMovieList(sortType, page);
                    if (movieList.isEmpty()) {
                        isLastPage = true;
                    } else if (movies.getValue() != null) {
                        List<Movie> newMoviewList = movies.getValue();
                        newMoviewList.addAll(movieList);
                        movies.postValue(newMoviewList);
                        page++;
                    } else {
                        movies.postValue(movieList);
                        page++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void setOptions(int sortType) {
        this.sortType = sortType;
        page = 1;
        isLastPage = false;
        movies.postValue(new ArrayList<>());
    }

    public void loadMore() {
        if (!isLastPage) {
            loadMovies();
        }
    }
}
