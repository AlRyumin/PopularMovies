package com.example.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.adapter.ReviewAdapter;
import com.example.popularmoviesapp.adapter.TrailerAdapter;
import com.example.popularmoviesapp.data.Constant;
import com.example.popularmoviesapp.database.AppDatabase;
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
import butterknife.OnClick;

public class DetailActivity extends BaseAppActivity implements TrailerAdapter.ListItemClickListener, ReviewAdapter.ReviewItemClickListener {
    private Movie movie;
    private TrailerAdapter adapter;
    private ReviewAdapter reviewAdapter;
    private List<Trailer> trailerList;
    private List<Review> reviewList;
    private boolean isFavorite;

    @BindView(R.id.im_backdrop)
    ImageView imBackdrop;
    @BindView(R.id.im_favorite)
    ImageView imFavorite;
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
    @BindView(R.id.trailers_logo)
    TextView trailersLogo;
    @BindView(R.id.reviews_logo)
    TextView reviewsLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        isFavorite = false;

        setData();
        setTrailersData();
        setReviewsData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfMoviewFavorite();
    }

    private void checkIfMoviewFavorite(){
        AppDatabase database = AppDatabase.getInstance(this);
        Thread thread = new Thread(){
            @Override
            public void run() {
                Movie favoriteMovie = database.movieDao().get(movie.getId());
                if(favoriteMovie != null){
                    runOnUiThread(() -> imFavorite.setImageResource(R.drawable.ic_favorite));
                    isFavorite = true;
                }
            }
        };
        thread.start();
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
        Thread thread = new Thread() {
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

                            if (trailerList.isEmpty()) {
                                trailersLogo.setVisibility(View.GONE);
                            }

                            if (reviewList.isEmpty()) {
                                reviewsLogo.setVisibility(View.GONE);
                            }
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
        tvVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        tvDate.setText(movie.getReleaseDate());

        getSupportActionBar().setTitle(movie.getTitle());
    }

    private String getTitle(Movie movie) {
        String title = movie.getTitle();
        String originalTitle = movie.getOriginalTitle();
        return title.equals(originalTitle) ? title : title + " (" + originalTitle + ")";
    }

    @OnClick(R.id.im_favorite)
    public void submit(View view) {
        isFavorite = !isFavorite;

        int imageId = isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
        String toastText = isFavorite ? getString(R.string.added_to_favorite)
                : getString(R.string.removed_from_favorite);

        imFavorite.setImageResource(imageId);

        updateDatabase(isFavorite);

        Toast toast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void updateDatabase(boolean isFavorite) {
        AppDatabase database = AppDatabase.getInstance(this);

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (isFavorite) {
                    saveMovieToFavorite(database);
                } else {
                    removeMovieFromFavorite(database);
                }
            }
        };

        thread.start();
    }

    private void saveMovieToFavorite(AppDatabase database) {
        database.movieDao().insertMovie(movie);
        sendMovieFavoriteStatus(true);
    }

    private void removeMovieFromFavorite(AppDatabase database) {
        database.movieDao().delete(movie);
        sendMovieFavoriteStatus(false);
    }

    private void sendMovieFavoriteStatus(boolean isFavorite){
        Intent intent = new Intent(Constant.MOVIE_FAVORITE_ACTION);
        intent.putExtra(Constant.MOVIE_FAVORITE_KEY, isFavorite);
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    @Override
    public void onListItemClick(int itemIndex) {
        Trailer trailer = trailerList.get(itemIndex);

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getVideoUrl())));
    }

    @Override
    public void onReviewItemClick(int itemIndex) {
        Review review = reviewList.get(itemIndex);
        PopupWindow popupWindow;
        View menuView = getLayoutInflater().inflate(R.layout.review_detail_window, null);

        TextView content = menuView.findViewById(R.id.content);
        TextView author = menuView.findViewById(R.id.author);
        ImageButton closeButton = menuView.findViewById(R.id.close_button);

        popupWindow = new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

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
