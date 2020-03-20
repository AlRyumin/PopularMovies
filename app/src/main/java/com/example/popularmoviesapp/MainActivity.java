package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utilities.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    ImageAdapter imageAdapter;
    @BindView(R.id.movie_list)
    GridView movieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
        loadData();
    }


    public void initView() {
        imageAdapter = new ImageAdapter(this);
        movieList.setAdapter(imageAdapter);
        final Context context = this;

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Movie movie = (Movie) imageAdapter.getItem(position);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Movie.class.getCanonicalName(), movie);

                startActivity(intent);
            }
        });
    }

    private void beforeLoadData() {

    }

    private void afterLoadData() {

    }

    private void loadData() {
        beforeLoadData();
        MyThread myThread = new MyThread();
        myThread.start();
    }

    public void setList(ArrayList<Movie> data) {
        imageAdapter.setData(data);
    }

    public class MyThread extends Thread {
        public void run() {
            try {
                ArrayList<Movie> movieList = JsonUtils.getMovieList();
                SetMovieList setMovieList = new SetMovieList(movieList);
                runOnUiThread(setMovieList);
                Log.d("movieList", movieList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            afterLoadData();
        }
    }

    public class SetMovieList extends Thread {
        ArrayList<Movie> list;

        public SetMovieList(ArrayList<Movie> list) {
            this.list = list;
        }

        public void run() {
            setList(list);
            afterLoadData();
        }
    }
}
