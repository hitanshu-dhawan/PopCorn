package com.hitanshudhawan.popcorn.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hitanshu on 9/8/17.
 *
 * Modified by Angelo on 19/04/19
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    public static final String FAV_MOVIES_TABLE_NAME = "FavouriteMoviesTable";
    public static final String FAV_TV_SHOWS_TABLE_NAME = "FavouriteTVShowsTable";
    public static final String FAV_PEOPLE_TABLE_NAME = "FavouritePeopleTable";

    public static final String ID = "id";
    public static final String MOVIE_ID = "movie_id";
    public static final String TV_SHOW_ID = "tv_show_id";
    public static final String PEOPLE_ID = "people_id";

    public static final String POSTER_PATH = "poster_path";
    public static final String NAME = "name";
    public static final String PROFILE_PATH = "profile_path";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreateMovieTable = "CREATE TABLE " + FAV_MOVIES_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ID + " INTEGER, "
                + POSTER_PATH + " TEXT, "
                + NAME + " TEXT )";
        String queryCreateTVShowTable = "CREATE TABLE " + FAV_TV_SHOWS_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TV_SHOW_ID + " INTEGER, "
                + POSTER_PATH + " TEXT, "
                + NAME + " TEXT )";
        String queryCreatePeopleTable = "CREATE TABLE " + FAV_PEOPLE_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PEOPLE_ID + " INTEGER, "
                + PROFILE_PATH + " TEXT, "
                + NAME + " TEXT )";
        sqLiteDatabase.execSQL(queryCreateMovieTable);
        sqLiteDatabase.execSQL(queryCreateTVShowTable);
        sqLiteDatabase.execSQL(queryCreatePeopleTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
