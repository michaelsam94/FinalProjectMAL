package com.example.android.finalprojectmal;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by michael on 29/08/16.
 */
public class FavouriteActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<Movie> movies;
    private FavouriteDataSource favouriteDataSource;
    MovieAdapter movieAdapter;
    int selected;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridview);
        favouriteDataSource = new FavouriteDataSource(this);
        favouriteDataSource.open();
        movies = favouriteDataSource.getAllMovies();
        movieAdapter = new MovieAdapter(this, movies);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FrameLayout detailFragment = (FrameLayout) findViewById(R.id.detailMovieFragment);
                Movie movie = (Movie) parent.getItemAtPosition(position);
                selected = position;
                if (detailFragment != null) {
                    MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                    movieDetailsFragment.setArguments(movie.toBundle());
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.detailMovieFragment, movieDetailsFragment).commit();
                } else {
                    Intent i = new Intent(FavouriteActivity.this, MovieDetailsActivity.class);
                    i.putExtra("movie", movie.toBundle());
                    i.putExtra("postion", position);
                    startActivity(i);
                }
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favouriteDataSource.close();
    }
}
