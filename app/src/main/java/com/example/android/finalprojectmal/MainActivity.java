package com.example.android.finalprojectmal;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private String sortType;
    private GridView gridView;

    int selected;
    //   int page;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("sortType", sortType);
        outState.putInt("selected", selected);

    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);

/*        selected = savedInstanceState.getInt("selected");
        sortType = (String) savedInstanceState.get("sortType");
        gridView.setSelection(selected);*/

        sortType = getSortType();
        ArrayList<Movie> movies = null;
        if(isOnline()){
            try {
                movies = new FetchMoviesTask(1, sortType).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            MovieAdapter movieAdapter = new MovieAdapter(this, movies);
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
                        Intent i = new Intent(MainActivity.this, MovieDetailsActivity.class);
                        i.putExtra("movie", movie.toBundle());
                        i.putExtra("postion", position);
                        startActivity(i);
                    }
                }

            });
        } else {
            Toast toast = new Toast(this);
            toast.makeText(this,"There is no internet to load movies",Toast.LENGTH_SHORT).show();
        }


        //load more than 20 movies

//        gridView.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) throws ExecutionException, InterruptedException {
//                Log.d("page",""+page);
//                ArrayList<Movie> movies = new FetchMoviesTask(page, sortType).execute().get();
//                MovieAdapter movieAdapter = (MovieAdapter) gridView.getAdapter();
//                movieAdapter.addAll(movies);
//                return true;
//            }
//
//
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        } else if(id == R.id.action_favourite) {
            Intent i = new Intent(this, FavouriteActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getSortType() {
        String sortType;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortType = prefs.getString("sortprefs", "top_rated");
        return sortType;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}




