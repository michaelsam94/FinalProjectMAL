package com.example.android.finalprojectmal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by michael on 28/08/16.
 */
public class FavouriteOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "favourite.db";

    public static final String DB_TABLE_MOVIES = "movies";
    public static final String DB_TABLE_REVIEWS = "reviews";
    public static final String DB_TABLE_TRAILERS = "trailers";

    public static final String MOVIE_ID_COLUMN = "movie_id";
    public static final String MOVIE_POSTER_IMAGE_COLUMN = "poster_image";
    public static final String MOVIE_DESCRIPTION_COLUMN = "description";
    public static final String MOVIE_TITLE_COLUMN = "title";
    public static final String MOVIE_RATE_COLUMN = "rate";
    public static final String MOVIE_RELEASE_DATE_COLUMN = "release_date";

    public static final String REVIEW_MOVIE_ID_COLUMN = "review_id";
    public static final String REVIEW_AUTHOR_COLUMN = "author";
    public static final String REVIEW_CONTENT_COLUMN = "content";

    public static final String TRAILER_MOVIE_ID_COLUMN = "trailer_id";
    public static final String TRAILER_YOUTUBE_LINK_COLUMN = "youtube_url";


    private static final String MOVIES_TABLE_CREATE = "create table " + DB_TABLE_MOVIES
            + " (" + MOVIE_ID_COLUMN + " text primary key, "
            + MOVIE_POSTER_IMAGE_COLUMN + " text not null, "
            + MOVIE_DESCRIPTION_COLUMN + " text not null, "
            + MOVIE_RATE_COLUMN + " text not null, "
            + MOVIE_RELEASE_DATE_COLUMN + " text not null, "
            + MOVIE_TITLE_COLUMN + " text not null " + ");";

    private static final String TRAILERS_TABLE_CREATE = "create table " + DB_TABLE_TRAILERS
            + " (" + TRAILER_MOVIE_ID_COLUMN + " text not null, "
            + TRAILER_YOUTUBE_LINK_COLUMN + " text not null " + ");";


    private static final String REVIEWS_TABLE_CREATE = "create table " + DB_TABLE_REVIEWS
            + " (" + REVIEW_MOVIE_ID_COLUMN + " text not null, "
            + REVIEW_AUTHOR_COLUMN + " text not null, "
            + REVIEW_CONTENT_COLUMN + " text not null " +");";




    public FavouriteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MOVIES_TABLE_CREATE);
        db.execSQL(TRAILERS_TABLE_CREATE);
        db.execSQL(REVIEWS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_TRAILERS);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_REVIEWS);
        onCreate(db);
    }
}
