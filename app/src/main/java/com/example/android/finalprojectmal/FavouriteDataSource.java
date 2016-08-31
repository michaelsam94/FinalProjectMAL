package com.example.android.finalprojectmal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by michael on 28/08/16.
 */
public class FavouriteDataSource {

    private SQLiteDatabase database;
    private FavouriteOpenHelper openHelper;
    private String[] moviesColumns = {
            FavouriteOpenHelper.MOVIE_ID_COLUMN,
            FavouriteOpenHelper.MOVIE_POSTER_IMAGE_COLUMN,
            FavouriteOpenHelper.MOVIE_DESCRIPTION_COLUMN,
            FavouriteOpenHelper.MOVIE_RATE_COLUMN,
            FavouriteOpenHelper.MOVIE_RELEASE_DATE_COLUMN,
            FavouriteOpenHelper.MOVIE_TITLE_COLUMN,
    };

    private String[] trailersColumns = {
            FavouriteOpenHelper.TRAILER_MOVIE_ID_COLUMN,
            FavouriteOpenHelper.TRAILER_YOUTUBE_LINK_COLUMN,
    };

    private String[] reviewsColumns = {
            FavouriteOpenHelper.REVIEW_MOVIE_ID_COLUMN,
            FavouriteOpenHelper.REVIEW_AUTHOR_COLUMN,
            FavouriteOpenHelper.REVIEW_CONTENT_COLUMN,
    };

    public FavouriteDataSource(Context context) {
        openHelper = new FavouriteOpenHelper(context);
    }


    public void open() throws SQLException {
        database = openHelper.getWritableDatabase();
    }

    public void close() {
        openHelper.close();
    }

    public int createMovie(Movie movie,Bitmap poster) throws SQLException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        poster.compress(Bitmap.CompressFormat.PNG, 100, bos);
        ContentValues values = new ContentValues();

        int sumOfRecords = 0;
        values.put(FavouriteOpenHelper.MOVIE_ID_COLUMN, movie.getId());
        values.put(FavouriteOpenHelper.MOVIE_POSTER_IMAGE_COLUMN, movie.getPosterURL());
        values.put(FavouriteOpenHelper.MOVIE_DESCRIPTION_COLUMN, movie.getDescription());
        values.put(FavouriteOpenHelper.MOVIE_RATE_COLUMN, movie.getRate());
        values.put(FavouriteOpenHelper.MOVIE_RELEASE_DATE_COLUMN, movie.getRelaseDate());
        values.put(FavouriteOpenHelper.MOVIE_TITLE_COLUMN, movie.getTitle());
        long insertId = database.insert(FavouriteOpenHelper.DB_TABLE_MOVIES, null, values);

        if(insertId == -1){
            return -1;
        }
        sumOfRecords++;
        values.clear();

        for(int i=0;i<movie.getYoutubeURLs().size();i++){
            values.put(FavouriteOpenHelper.TRAILER_MOVIE_ID_COLUMN, movie.getId());
            values.put(FavouriteOpenHelper.TRAILER_YOUTUBE_LINK_COLUMN,movie.getYoutubeURLs().get(i));
            insertId = database.insert(FavouriteOpenHelper.DB_TABLE_TRAILERS, null, values);
            if(insertId==-1){
                return -1;
            }
            sumOfRecords++;
        }

        values.clear();

        for(int i=0;i<movie.getReviewsAutors().size();i++){
            values.put(FavouriteOpenHelper.REVIEW_MOVIE_ID_COLUMN, movie.getId());
            values.put(FavouriteOpenHelper.REVIEW_AUTHOR_COLUMN,movie.getReviewsAutors().get(i));
            values.put(FavouriteOpenHelper.REVIEW_CONTENT_COLUMN,movie.getReviewsContents().get(i));
            insertId = database.insert(FavouriteOpenHelper.DB_TABLE_REVIEWS, null, values);
            if(insertId==-1){
                return -1;
            }
            sumOfRecords++;
        }
        return sumOfRecords;
    }

    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> favouritMovies = new ArrayList<>();

        Cursor cursor = database.query(FavouriteOpenHelper.DB_TABLE_MOVIES,moviesColumns,
                null,null,null,null,null);
        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount();i++){
            Movie movie = new Movie();
            movie.movieCursorToMovie(cursor);
            cursor.moveToNext();
            favouritMovies.add(movie);
        }

        for (int i=0;i<favouritMovies.size();i++){
            String whereClause = FavouriteOpenHelper.TRAILER_MOVIE_ID_COLUMN + " = " + favouritMovies.get(i).getId();
            cursor = database.query(FavouriteOpenHelper.DB_TABLE_TRAILERS,trailersColumns,
                    whereClause, null, null,null,null);
            cursor.moveToFirst();
            favouritMovies.get(i).trailersCursorToMovie(cursor,favouritMovies.get(i));
        }

        for (int i=0;i<favouritMovies.size();i++){
            String whereClause = FavouriteOpenHelper.REVIEW_MOVIE_ID_COLUMN + " = " + favouritMovies.get(i).getId();
            cursor = database.query(FavouriteOpenHelper.DB_TABLE_REVIEWS,reviewsColumns,
                    whereClause, null, null,null,null);
            cursor.moveToFirst();
            favouritMovies.get(i).reviewsCursorToMovie(cursor,favouritMovies.get(i));
        }
        cursor.close();
        return favouritMovies;
    }




}
