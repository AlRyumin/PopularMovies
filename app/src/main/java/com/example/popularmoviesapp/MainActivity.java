package com.example.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmoviesapp.data.Constant;
import com.example.popularmoviesapp.data.MoviePreferences;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAppActivity {
    ImageAdapter imageAdapter;
    @BindView(R.id.movie_list)
    GridView movieList;
    int sortType;
    MainViewModel viewModel;
    boolean isLoading = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        sortType = MoviePreferences.sortType(this);

        initView(savedInstanceState);

        if (NetworkUtils.isOnline()) {
            hideNetworkError();
            setViewModel();
        } else {
            showNetworkError();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setViewModel(){
        viewModel = new ViewModelProvider(this, getDefaultViewModelProviderFactory()).get(MainViewModel.class);
        viewModel.setOptions(sortType);

        viewModel.getMovies().observe(this, movies -> {
            setList(movies);
            isLoading = false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        int sortType = MoviePreferences.sortType(this);
        if (this.sortType != sortType) {
            this.sortType = sortType;
            viewModel.setOptions(sortType);
            isLoading = true;
        }

        if (sortType == Constant.SORT_TYPE_POPULAR) {
            getSupportActionBar().setTitle(R.string.most_popular_title);
        } else if (sortType == Constant.SORT_TYPE_TOP_RATED) {
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

        movieList.setOnItemClickListener((parent, v, position, id) -> {
            Movie movie = (Movie) imageAdapter.getItem(position);
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Movie.class.getCanonicalName(), movie);

            startActivity(intent);
        });

        movieList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && viewModel != null &&
                        !isLoading) {
                    viewModel.loadMore();
                    isLoading = true;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        Log.d("MainD", "SAVEINST");
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("imageAdapter", imageAdapter.getItems());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setList(List<Movie> data) {
        Log.d("SetLis", "adf");
        if (data != null) {
            imageAdapter.setData(data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.network_error_btn)
    public void submit(View view) {
        if(NetworkUtils.isOnline()){
            hideNetworkError();
            setViewModel();
        } else {
            Toast toast = Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
