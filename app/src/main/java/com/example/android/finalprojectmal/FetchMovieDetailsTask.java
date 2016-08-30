package com.example.android.finalprojectmal;

import android.os.AsyncTask;


/**
 * Created by michael on 23/08/16.
 */
public class FetchMovieDetailsTask extends AsyncTask<Void,Void,Movie> {
    Movie movie;


    FetchMovieDetailsTask(Movie movie){
        this.movie = movie;
    }


    @Override
    protected Movie doInBackground(Void... params) {

        JsonHandler jsonHandler = new JsonHandler();
        jsonHandler.setTrailerURLToMovie(movie);
        jsonHandler.setReviewsToMovie(movie);



        return movie;
    }

}
