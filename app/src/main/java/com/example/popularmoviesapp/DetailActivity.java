package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.im_backdrop) ImageView imBackdrop;
    @BindView(R.id.tv_movie_title) TextView tvMovieTitle;
    @BindView(R.id.tv_synopsis) TextView tvSynopsis;
    @BindView(R.id.tv_vote_average) TextView tvVoteAverage;
    @BindView(R.id.tv_date) TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Movie movie = (Movie) getIntent().getParcelableExtra(
                Movie.class.getCanonicalName());

        Picasso.get().load(movie.getBackdrop()).placeholder(R.drawable.empty_backdrop).into(imBackdrop);
        imBackdrop.setAdjustViewBounds(true);

        tvMovieTitle.setText(movie.getTitle() + " (" + movie.getOriginal_title() + ")");
        tvSynopsis.setText(movie.getSynopsis());
        tvVoteAverage.setText(String.valueOf(movie.getVote_average()));
        tvDate.setText(movie.getReleaseDate());

        Log.d("movietitel", movie.getTitle());
    }
}
