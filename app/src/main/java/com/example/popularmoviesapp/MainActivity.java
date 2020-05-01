package com.example.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.lifecycle.ViewModelProvider;

import com.example.popularmoviesapp.data.Constant;
import com.example.popularmoviesapp.data.MoviePreferences;
import com.example.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppActivity {
    ImageAdapter imageAdapter;
    @BindView(R.id.movie_list)
    GridView movieList;
    int sortType;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        sortType = MoviePreferences.sortType(this);

        initView(savedInstanceState);

        viewModel = new ViewModelProvider(this, getDefaultViewModelProviderFactory()).get(MainViewModel.class);
        viewModel.setSortType(sortType);

        viewModel.getMovies().observe(this, movies -> {
            setList(movies);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        int sortType = MoviePreferences.sortType(this);
        Log.d("SORTTYP", Integer.toString(sortType));
        Log.d("SORTTYPTH", Integer.toString(this.sortType));
        if(this.sortType != sortType){
            this.sortType = sortType;
            viewModel.setSortType(sortType);
        }

        if (sortType == Constant.SORT_TYPE_POPULAR) {
            getSupportActionBar().setTitle(R.string.most_popular_title);
        } else if(sortType == Constant.SORT_TYPE_TOP_RATED){
            getSupportActionBar().setTitle(R.string.top_rated_title);
        } else {
            getSupportActionBar().setTitle(R.string.favorite_title);
        }
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

    public void setList(List<Movie> data) {
        if(data != null){
            imageAdapter.setData(data);
        }
    }
}
