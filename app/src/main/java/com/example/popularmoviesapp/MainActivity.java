package com.example.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.popularmoviesapp.data.Constant;
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

        initView(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int sortType = MoviePreferences.sortType(this);
        if (sortType == Constant.SORT_TYPE_POPULAR) {
            getSupportActionBar().setTitle(R.string.most_popular_title);
        } else {
            getSupportActionBar().setTitle(R.string.top_rated_title);
        }
        loadData(sortType);
    }

    public void initView(Bundle savedInstanceState) {
        imageAdapter = new ImageAdapter(this);
        if(savedInstanceState != null) {
            ArrayList<Movie> items = savedInstanceState.getParcelableArrayList("imageAdapter");
            imageAdapter.setItems(items); // Load saved data if any.
        }
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

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("imageAdapter", imageAdapter.getItems());
    }

    private void loadData(int sortType) {
        if (NetworkUtils.isOnline()) {
            hideNetworkError();
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
        }
    }

    public class SetMovieList extends Thread {
        ArrayList<Movie> list;

        public SetMovieList(ArrayList<Movie> list) {
            this.list = list;
        }

        public void run() {
            setList(list);
        }
    }
}
