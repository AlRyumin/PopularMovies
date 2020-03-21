package com.example.popularmoviesapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseAppActivity {
    @BindView(R.id.im_backdrop)
    ImageView imBackdrop;
    @BindView(R.id.tv_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.tv_synopsis)
    TextView tvSynopsis;
    @BindView(R.id.tv_vote_average)
    TextView tvVoteAverage;
    @BindView(R.id.tv_date)
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setData();
    }

    private void setData() {
        if (NetworkUtils.isOnline()) {
            setMovieData();
            hideNetworkError();
        } else {
            showNetworkError();
        }
    }

    private void setMovieData() {
        Movie movie = (Movie) getIntent().getParcelableExtra(
                Movie.class.getCanonicalName());

        Picasso.get().load(movie.getBackdrop()).placeholder(R.drawable.empty_backdrop).into(imBackdrop);
        imBackdrop.setAdjustViewBounds(true);
        imBackdrop.setScaleType(ImageView.ScaleType.FIT_XY);

        tvMovieTitle.setText(getTitle(movie));
        tvSynopsis.setText(movie.getSynopsis());
        tvVoteAverage.setText(String.valueOf(movie.getVote_average()));
        tvDate.setText(movie.getReleaseDate());

        getSupportActionBar().setTitle(movie.getTitle());
    }

    private String getTitle(Movie movie) {
        String title = movie.getTitle();
        String originalTitle = movie.getOriginalTitle();
        String showTitle = title.equals(originalTitle) ? title : title + " (" + originalTitle + ")";

        return showTitle;
    }
}
