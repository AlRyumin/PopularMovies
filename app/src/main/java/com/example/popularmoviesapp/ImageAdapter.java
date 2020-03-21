package com.example.popularmoviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.popularmoviesapp.draweble.TextDrawable;
import com.example.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Movie> items;
    private Context mContext;

    public ImageAdapter(Context context) {
        items = new ArrayList<>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Movie movie = items.get(i);

        TextDrawable textDrawable = new TextDrawable(movie.getTitle());

        Picasso.get().load(movie.getPoster())
                .placeholder(textDrawable)
                .into(imageView);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return imageView;
    }

    public void setData(ArrayList<Movie> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
