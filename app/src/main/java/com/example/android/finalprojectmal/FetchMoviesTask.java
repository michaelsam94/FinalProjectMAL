package com.example.android.finalprojectmal;


import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by michael on 11/08/16.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private ArrayList<Movie> movies;
    private String sortType;
    private int page;


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
