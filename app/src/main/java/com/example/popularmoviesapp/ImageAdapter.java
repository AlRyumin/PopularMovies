package com.example.popularmoviesapp;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.popularmoviesapp.drawable.TextDrawable;
import com.example.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageAdapter extends BaseAdapter implements Parcelable {
    private List<Movie> items;
    private Context mContext;

    public ImageAdapter(Context context) {
        items = new ArrayList<>();
        mContext = context;
    }

    private ImageAdapter(Parcel parcel) {
        items = parcel.readArrayList(ImageAdapter.class.getClassLoader());
    }

    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }

    public ArrayList<Movie> getItems() {
        return (ArrayList<Movie>) items;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData(List<Movie> items) {
        this.items = items;
//        this.items = Stream.concat(items.stream(), this.items.stream())
//                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(items);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ImageAdapter createFromParcel(Parcel parcel) {
            return new ImageAdapter(parcel);
        }

        public ImageAdapter[] newArray(int size) {
            return new ImageAdapter[size];
        }
    };
}
