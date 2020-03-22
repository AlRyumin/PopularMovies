package com.example.popularmoviesapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.popularmoviesapp.R;

public class MoviePreferences {
    public static int sortType(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pref_sort_order);
        String defaultSort = context.getString(R.string.pref_most_popular);
        String preferredUnits = prefs.getString(key, defaultSort);
        String sort = context.getString(R.string.pref_most_popular);
        int type;
        if (sort.equals(preferredUnits)) {
            type = Constant.SORT_TYPE_POPULAR;
        } else {
            type = Constant.SORT_TYPE_TOP_RATED;
        }
        return type;
    }
}
