package com.example.popularmoviesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmoviesapp.data.Constant;
import com.example.popularmoviesapp.data.MoviePreferences;
import com.example.popularmoviesapp.database.AppDatabase;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utilities.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAppActivity {
    ImageAdapter imageAdapter;
    @BindView(R.id.movie_list)
    GridView movieList;
    @BindView(R.id.list_title)
    TextView listTitle;
    int sortType;
    MainViewModel viewModel;
    boolean isLoading = false;
    boolean isFavorite = false;

    private BroadcastReceiver movieFavoriteStatusReceiver;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        sortType = MoviePreferences.sortType(this);

        initView();

        if (NetworkUtils.isOnline()) {
            hideNetworkError();
            setViewModel();
        } else {
            showNetworkError();
        }

        movieFavoriteStatusReceiver = new MovieFavoriteStatusReceiver(this);

        IntentFilter intentFilter = new IntentFilter(Constant.MOVIE_FAVORITE_ACTION);
        registerReceiver(movieFavoriteStatusReceiver,  intentFilter);
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
            listTitle.setText(getText(R.string.most_popular_title));
        } else if (sortType == Constant.SORT_TYPE_TOP_RATED) {
            listTitle.setText(getText(R.string.top_rated_title));
        } else {
            listTitle.setText(getText(R.string.favorite_title));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initView() {
        imageAdapter = new ImageAdapter(this);
        isFavorite = false;

        movieList.setAdapter(imageAdapter);
        final Context context = this;

        movieList.setOnItemClickListener((parent, v, position, id) -> {
            Movie movie = (Movie) imageAdapter.getItem(position);
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Movie.class.getCanonicalName(), movie);

            checkIsFavorite(context, movie);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setList(List<Movie> data) {
        if (data != null) {
            imageAdapter.setData(data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.network_error_btn)
    public void submit(View view) {
        if (NetworkUtils.isOnline()) {
            hideNetworkError();
            setViewModel();
        } else {
            Toast toast = Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(movieFavoriteStatusReceiver);
    }

    public void updateIsFavorite(boolean isFavorite){
        if (this.sortType == Constant.SORT_TYPE_FAVORITE_MOVIES) {
            this.isFavorite = isFavorite;
            viewModel.dBUpdated();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setViewModel() {
        viewModel = new ViewModelProvider(this, getDefaultViewModelProviderFactory()).get(MainViewModel.class);
        viewModel.setOptions(sortType);

        viewModel.getMovies().observe(this, movies -> {
            setList(movies);
            isLoading = false;
        });
    }

    private void checkIsFavorite(Context context, Movie movie){
        Thread thread = new Thread(() -> {
            AppDatabase database = AppDatabase.getInstance(context);
            Movie dBMovie = database.movieDao().get(movie.getId());
            if (dBMovie != null) {
                isFavorite = true;
            } else {
                isFavorite = false;
            }
        });
        thread.start();
    }

    class MovieFavoriteStatusReceiver extends BroadcastReceiver {
        MainActivity mainActivity = null;

        public MovieFavoriteStatusReceiver(MainActivity main){
            mainActivity = main;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isFavorite = intent.getBooleanExtra(Constant.MOVIE_FAVORITE_KEY, false);
            if (mainActivity.isFavorite != isFavorite) {
                mainActivity.updateIsFavorite(isFavorite);
            }
        }
    }
}
