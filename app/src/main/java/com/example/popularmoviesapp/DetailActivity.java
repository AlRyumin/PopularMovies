package com.example.popularmoviesapp;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.adapter.ReviewAdapter;
import com.example.popularmoviesapp.adapter.TrailerAdapter;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.model.Review;
import com.example.popularmoviesapp.model.Trailer;
import com.example.popularmoviesapp.utilities.JsonUtils;
import com.example.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseAppActivity implements TrailerAdapter.ListItemClickListener, ReviewAdapter.ReviewItemClickListener {
    private Movie movie;
    private TrailerAdapter adapter;
    private ReviewAdapter reviewAdapter;
    private List<Trailer> trailerList;
    private List<Review> reviewList;

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
    @BindView(R.id.trailer_list)
    RecyclerView trailers;
    @BindView(R.id.review_list)
    RecyclerView reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setData();
        setTrailersData();
        setReviewsData();
    }

    private void setTrailersData() {
        adapter = new TrailerAdapter(this);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        trailers.setLayoutManager(layoutManager);

        trailers.setAdapter(adapter);
    }

    private void setReviewsData() {
        reviewAdapter = new ReviewAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviews.setLayoutManager(layoutManager);

        reviews.setAdapter(reviewAdapter);
    }

    private void setData() {
        if (NetworkUtils.isOnline()) {
            setMovieData();
            setDetailData();
            hideNetworkError();
        } else {
            showNetworkError();
        }
    }

    private void setDetailData() {
        Log.d("downloadData", "asdfasf");
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    trailerList = JsonUtils.getTrailer(movie.getId());
                    reviewList = JsonUtils.getReviews(movie.getId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setTrailerList(trailerList);
                            reviewAdapter.setReviewList(reviewList);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void setMovieData() {
        movie = getIntent().getParcelableExtra(
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
        return title.equals(originalTitle) ? title : title + " (" + originalTitle + ")";
    }

    @Override
    public void onListItemClick(int itemIndex) {
        Trailer trailer = trailerList.get(itemIndex);
        Log.d("oncliasdfas", trailer.getVideoUrl());

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getVideoUrl())));
    }

    @Override
    public void onReviewItemClick(int itemIndex) {
        Review review = reviewList.get(itemIndex);
        PopupWindow popupWindow;
        View menuView=getLayoutInflater().inflate(R.layout.review_detail_window, null);

        TextView content = menuView.findViewById(R.id.content);
        TextView author = menuView.findViewById(R.id.author);
        ImageButton closeButton = menuView.findViewById(R.id.close_button);

        popupWindow=new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        popupWindow.showAtLocation(menuView, Gravity.TOP | Gravity.RIGHT, 0, 0);

        content.setText(review.getContent());
        author.setText(review.getAuthor());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });


    }
}
