package com.example.android.finalprojectmal;

import android.database.Cursor;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by michael on 13/08/16.
 */
public class Movie implements Serializable {

    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_DESCRIPTION = "releaseDate";
    public static final String MOVIE_RELASEDATE = "description";
    public static final String MOVIE_POSTERURL = "posterURL";
    public static final String MOVIE_RATE = "rate";
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_TRAILER_URL = "trailerURL";
    public static final String MOVIE_YOUTUBE_URLS = "youtubeURLs";
    public static final String MOVIE_REVIEW_URL = "reviewURL";
    public static final String MOVIE_REVIEWS_AUTORS = "reviewsAutors";
    public static final String MOVIE_REVIEWS_CONTENTS = "reviewsContents";




    private String title;
    private String relaseDate;
    private String description;
    private String posterURL;
    private String rate;
    private String id;
    private String trailerURL;
    private String reviewURL;
    private ArrayList<String> reviewsAutors;
    private ArrayList<String> reviewsContents;
    private ArrayList<String> youtubeURLs;


    //_______________________________________________________________
    //constructors

    public Movie() {
        this.title = "";
        this.relaseDate = "";
        this.description = "";
        this.posterURL = "";
        this.rate = "";
        this.id = "";
        this.trailerURL = "";
        this.reviewURL = "";

    }


    public Movie(Bundle b) {
        this.title = b.getString(MOVIE_TITLE);
        this.relaseDate = b.getString(MOVIE_RELASEDATE);
        this.description = b.getString(MOVIE_DESCRIPTION);
        this.rate = b.getString(MOVIE_RATE);
        this.posterURL = b.getString(MOVIE_POSTERURL);
        this.id = b.getString(MOVIE_ID);
        this.trailerURL = b.getString(MOVIE_TRAILER_URL);
        this.reviewURL = b.getString(MOVIE_REVIEW_URL);
        this.reviewsAutors = b.getStringArrayList(MOVIE_REVIEWS_AUTORS);
        this.reviewsContents = b.getStringArrayList(MOVIE_REVIEWS_CONTENTS);
        this.youtubeURLs = b.getStringArrayList(MOVIE_YOUTUBE_URLS);

    }



    //_________________________________________________________________
    //setters

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setRelaseDate(String relaseDate) {
        this.relaseDate = relaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public void setYoutubeURLs(ArrayList<String> youtubeURLs) {
        this.youtubeURLs = youtubeURLs;
    }

    public void setReviewURL(String reviewURL) {
        this.reviewURL = reviewURL;
    }

    public void setReviewsAutors(ArrayList<String> reviewsAutors) {
        this.reviewsAutors = reviewsAutors;
    }

    public void setReviewsContents(ArrayList<String> reviewsContents) {
        this.reviewsContents = reviewsContents;
    }



    //_________________________________________________________________
    //getters

    public String getTitle() {
        return title;
    }

    public String getRelaseDate() {
        return relaseDate;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getRate() {
        return rate;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getYoutubeURLs() {
        return youtubeURLs;
    }

    public String getReviewURL() {
        return reviewURL;
    }

    public ArrayList<String> getReviewsAutors() {
        return reviewsAutors;
    }

    public ArrayList<String> getReviewsContents() {
        return reviewsContents;
    }



    //________________________________________________________________
    //convert object to bundle

    public Bundle toBundle(){
        Bundle b = new Bundle();
        b.putString(MOVIE_TITLE,this.title);
        b.putString(MOVIE_RELASEDATE,this.relaseDate);
        b.putString(MOVIE_RATE,this.rate);
        b.putString(MOVIE_DESCRIPTION,this.description);
        b.putString(MOVIE_POSTERURL,this.posterURL);
        b.putString(MOVIE_ID,this.id);
        b.putString(MOVIE_TRAILER_URL,this.trailerURL);
        b.putString(MOVIE_REVIEW_URL,this.reviewURL);
        b.putStringArrayList(MOVIE_REVIEWS_AUTORS,this.reviewsAutors);
        b.putStringArrayList(MOVIE_REVIEWS_CONTENTS,this.reviewsContents);
        b.putStringArrayList(MOVIE_YOUTUBE_URLS,this.youtubeURLs);
        return b;
    }

    public Movie movieCursorToMovie(Cursor c){
        this.id = c.getString(0);
        this.posterURL = c.getString(1);
        this.description = c.getString(2);
        this.rate = c.getString(3);
        this.relaseDate = c.getString(4);
        this.title = c.getString(5);
        return this;
    }

    public Movie trailersCursorToMovie(Cursor c,Movie movie){
        ArrayList<String> urls = new ArrayList<>();
        while (!c.isAfterLast()) {
            String url = c.getString(1);
            urls.add(url);
            c.moveToNext();
        }
        movie.setYoutubeURLs(urls);

        return movie;
    }


    public Movie reviewsCursorToMovie(Cursor c,Movie movie){
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> contentes = new ArrayList<>();
        while (!c.isAfterLast()) {
            String author = c.getString(1);
            String content = c.getString(2);
            authors.add(author);
            contentes.add(content);
            c.moveToNext();
        }
        movie.setReviewsAutors(authors);
        movie.setReviewsContents(contentes);
        return movie;
    }

//    public ArrayList<Bitmap> getMovieImagesFromCursor(Cursor c){
//        ArrayList<Bitmap> posters = new ArrayList<>();
//        while (!c.isAfterLast()) {
//            Bitmap poster = BitmapFactory.decodeByteArray(c.getBlob(1), 0, c.getBlob(1).length);
//            posters.add(poster);
//            c.moveToNext();
//        }
//
//        return posters;
//    }







}
