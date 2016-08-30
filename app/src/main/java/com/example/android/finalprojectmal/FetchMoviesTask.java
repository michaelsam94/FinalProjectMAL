package com.example.android.finalprojectmal;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by michael on 11/08/16.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private Context context;
    private static GridView grid;
    private static MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private String sortType;
    private int page;




    public FetchMoviesTask(Context context,View grid,int page, String sortType) {
        this.grid = (GridView) grid;
        this.sortType = sortType;
        this.page = page;
        this.context = context;
    }
    public FetchMoviesTask(int page, String sortType) {
        this.sortType = sortType;
        this.page = page;

    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        JsonHandler jsonHandler = new JsonHandler();
        movies = jsonHandler.setJsonBasicDetailsToMovie(page,sortType);


        return movies;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);


    }




}
