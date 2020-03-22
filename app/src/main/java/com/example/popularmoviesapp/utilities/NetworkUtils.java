package com.example.popularmoviesapp.utilities;

import android.net.Uri;

import com.example.popularmoviesapp.data.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {
    private static final String format = "json";

    private static final String POPULAR_URL =
            "http://api.themoviedb.org/3/movie/popular?api_key=" + Constant.API_KEY;

    private static final String TOP_RATED_URL =
            "http://api.themoviedb.org/3/movie/top_rated?api_key=" + Constant.API_KEY;

    public static URL buildUrl(String link) {

        Uri builtUri = Uri.parse(link)
                .buildUpon()
                .appendQueryParameter("mode", format)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getMovieList(int type) {
        String movies = null;
        String urlString = type == Constant.SORT_TYPE_POPULAR ? POPULAR_URL: TOP_RATED_URL;
        try {
            URL url = buildUrl(urlString);
            movies = getResponseFromHttpUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
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
