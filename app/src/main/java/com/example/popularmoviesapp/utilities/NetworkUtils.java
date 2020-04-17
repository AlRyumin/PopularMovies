package com.example.popularmoviesapp.utilities;

import android.util.Log;

import com.example.popularmoviesapp.data.Constant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {
    private static final String format = "json";

    private static final String POPULAR_URL =
            "http://api.themoviedb.org/3/movie/popular?api_key=" + Constant.API_KEY;

    private static final String TOP_RATED_URL =
            "http://api.themoviedb.org/3/movie/top_rated?api_key=" + Constant.API_KEY;

    private static OkHttpClient client = new OkHttpClient();

    public static String getMovieList(int type) {
        String movies = null;
        String url = type == Constant.SORT_TYPE_POPULAR ? POPULAR_URL : TOP_RATED_URL;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            movies = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static String getTrailers(Long id) {
        String trailers = null;
        String url = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + Constant.API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            trailers = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trailers;
    }

    public static String getReviews(Long id) {
        String reviews = null;
        String url = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + Constant.API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();
Log.d("urlasdf", url);
        try (Response response = client.newCall(request).execute()) {
            reviews = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
