package com.example.popularmoviesapp.utilities;

import com.example.popularmoviesapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class JsonUtils {
    public static ArrayList<Movie> getMovieList(int type) throws JSONException {
        ArrayList<Movie> items = new ArrayList<>();
        String urlBase = "http://image.tmdb.org/t/p/w185/";
        String urlBasew500 = "http://image.tmdb.org/t/p/w500/";
        String jsonString = NetworkUtils.getMovieList(type);

        JSONObject response = new JSONObject(jsonString);
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = (JSONObject) results.get(i);
            String poster = urlBase + result.getString("poster_path");
            String backdrop = urlBasew500 + result.getString("backdrop_path");
            Long id = result.getLong("id");
            String title = result.getString("title");
            String original_title = result.getString("original_title");
            String synopsis = result.getString("overview");
            String release_date = result.getString("release_date");
            Double vote_average = result.getDouble("vote_average");

            Movie movie = new Movie(id, title, original_title, poster, backdrop, synopsis, release_date, vote_average);
            items.add(movie);
        }

        return items;
    }
}
