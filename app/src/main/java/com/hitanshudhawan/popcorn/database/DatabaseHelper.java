package com.hitanshudhawan.popcorn.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hitanshu on 9/8/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String FAV_MOVIES_TABLE_NAME = "FavouriteMoviesTable";
    public static final String MOVIE_ID = "id";
    public static final String FAV_TV_SHOWS_TABLE_NAME = "FavouriteTVShowsTable";
    public static final String TV_SHOW_ID = "id";
    private static final String DATABASE_NAME = "database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreateMovieTable = "CREATE TABLE " + FAV_MOVIES_TABLE_NAME + " ( "
                + MOVIE_ID + " INTEGER NOT NULL PRIMARY KEY )";
        String queryCreateTVShowTable = "CREATE TABLE " + FAV_TV_SHOWS_TABLE_NAME + " ( "
                + TV_SHOW_ID + " INTEGER NOT NULL PRIMARY KEY )";
        sqLiteDatabase.execSQL(queryCreateMovieTable);
        sqLiteDatabase.execSQL(queryCreateTVShowTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
