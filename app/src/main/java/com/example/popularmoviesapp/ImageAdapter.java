package com.example.popularmoviesapp;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
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

    public void setItems(List<Movie> items) {
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


        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (view == null) {


            gridView = new View(mContext);

            gridView = inflater.inflate(R.layout.main_banner_item, null);

//            ImageView imageView = new ImageView(mContext);
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Movie movie = items.get(i);



            TextDrawable textDrawable = new TextDrawable(movie.getTitle());

            Picasso.get().load(movie.getPoster())
                    .placeholder(textDrawable)
                    .into(imageView);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setBackground(mContext.getDrawable(R.drawable.main_banner_background));
            imageView.setClipToOutline(true);


        } else {
            gridView = (View) view;
        }



        return gridView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData(List<Movie> items) {
        this.items = items;
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
