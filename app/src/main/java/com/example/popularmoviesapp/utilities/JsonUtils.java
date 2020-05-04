package com.example.popularmoviesapp.utilities;

import android.content.Intent;
import android.util.Log;

import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.model.Review;
import com.example.popularmoviesapp.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class JsonUtils {
    private static int movieType;

    public static ArrayList<Movie> getMovieList(int type, int page) throws JSONException {
        ArrayList<Movie> items = new ArrayList<>();
        String urlBase = "https://image.tmdb.org/t/p/w185/";
        String urlBasew500 = "https://image.tmdb.org/t/p/w500/";
        String jsonString = NetworkUtils.getMovieList(type, page);

        JSONObject response = new JSONObject(jsonString);
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            try {
                JSONObject result = (JSONObject) results.get(i);
                String poster = urlBase + result.getString("poster_path");
                String backdrop = urlBasew500 + result.getString("backdrop_path");
                Long id = result.getLong("id");
                String title = result.getString("title");
                String originalTitle = result.getString("original_title");
                String synopsis = result.getString("overview");
                String releaseDate = result.getString("release_date");
                Double voteAverage = result.getDouble("vote_average");

                Movie movie = new Movie(id, title, originalTitle, poster, backdrop, synopsis, releaseDate, voteAverage);
                items.add(movie);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return items;
    }

    private static void setMovieData(){

    }

    public static ArrayList<Trailer> getTrailer(Long id) throws JSONException {
        ArrayList<Trailer> items = new ArrayList<>();
        String jsonString = NetworkUtils.getTrailers(id);

        JSONObject response = new JSONObject(jsonString);
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = (JSONObject) results.get(i);
            String trailerId = result.getString("id");
            String key = result.getString("key");
            String name = result.getString("name");
            String site = result.getString("site");
            Integer size = result.getInt("size");
            String type = result.getString("type");
            Trailer trailer = new Trailer(trailerId, key, name, site, size, type, id);
            items.add(trailer);
        }

        return items;
    }

    public static ArrayList<Review> getReviews(Long id) throws JSONException {
        ArrayList<Review> items = new ArrayList<>();
        String jsonString = NetworkUtils.getReviews(id);

        JSONObject response = new JSONObject(jsonString);
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = (JSONObject) results.get(i);

            String reviewId = result.getString("id");
            String author = result.getString("author");
            String content = result.getString("content");
            String url = result.getString("url");

            Review review = new Review(reviewId, author, content, url);
            items.add(review);
        }

        return items;
    }
}
