package com.example.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.popularmoviesapp.data.MoviePreferences;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utilities.JsonUtils;
import com.example.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppActivity {
    ImageAdapter imageAdapter;
    @BindView(R.id.movie_list)
    GridView movieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int sortType = MoviePreferences.sortType(this);
        if (sortType == 1) {
            getSupportActionBar().setTitle(R.string.pref_most_popular_label);
        } else {
            getSupportActionBar().setTitle(R.string.pref_top_rated_label);
        }
        loadData(sortType);
    }

    public void initView() {
        imageAdapter = new ImageAdapter(this);
        movieList.setAdapter(imageAdapter);
        final Context context = this;

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movie = (Movie) imageAdapter.getItem(position);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Movie.class.getCanonicalName(), movie);

                startActivity(intent);
            }
        });
    }

    private void beforeLoadData() {

    }

    private void afterLoadData() {

    }

    private void loadData(int sortType) {
        if (NetworkUtils.isOnline()) {
            hideNetworkError();
            beforeLoadData();
            MovieThread movieThread = new MovieThread(sortType);
            movieThread.start();
        } else {
            showNetworkError();
        }
    }

    public void setList(ArrayList<Movie> data) {
        imageAdapter.setData(data);
    }

    public class MovieThread extends Thread {
        private int sortType;

        public MovieThread(int sortType) {
            this.sortType = sortType;
        }

        public void run() {
            try {
                ArrayList<Movie> movieList = JsonUtils.getMovieList(sortType);
                SetMovieList setMovieList = new SetMovieList(movieList);
                runOnUiThread(setMovieList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            afterLoadData();
        }
    }

    public class SetMovieList extends Thread {
        ArrayList<Movie> list;

        public SetMovieList(ArrayList<Movie> list) {
            this.list = list;
        }

        public void run() {
            setList(list);
            afterLoadData();
        }
    }
}
