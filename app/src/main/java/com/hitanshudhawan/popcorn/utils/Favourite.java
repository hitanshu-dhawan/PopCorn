package com.hitanshudhawan.popcorn.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hitanshudhawan.popcorn.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitanshu on 9/8/17.
 */

public class Favourite {

    //MOVIES

    public static void addMovieToFav(Context context, Integer movieId) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (!isMovieFav(context, movieId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.MOVIE_ID, movieId);
            database.insert(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static void removeMovieFromFav(Context context, Integer movieId) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isMovieFav(context, movieId)) {
            database.delete(DatabaseHelper.FAV_MOVIES_TABLE_NAME, DatabaseHelper.MOVIE_ID + " = " + movieId, null);
        }
        database.close();
    }

    public static boolean isMovieFav(Context context, Integer movieId) {
        if (movieId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isMovieFav;
        Cursor cursor = database.query(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, DatabaseHelper.MOVIE_ID + " = " + movieId, null, null, null, null);
        if (cursor.getCount() == 1)
            isMovieFav = true;
        else
            isMovieFav = false;

        cursor.close();
        database.close();
        return isMovieFav;
    }

    public static List<Integer> getFavMovieIds(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Integer> favMovieIds = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            favMovieIds.add(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MOVIE_ID)));
        }
        cursor.close();
        database.close();
        return favMovieIds;
    }

    //TV SHOWS

    public static void addTVShowToFav(Context context, Integer tvShowId) {
        if (tvShowId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (!isTVShowFav(context, tvShowId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.TV_SHOW_ID, tvShowId);
            database.insert(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static void removeTVShowFromFav(Context context, Integer tvShowId) {
        if (tvShowId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isTVShowFav(context, tvShowId)) {
            database.delete(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, DatabaseHelper.TV_SHOW_ID + " = " + tvShowId, null);
        }
        database.close();
    }

    public static boolean isTVShowFav(Context context, Integer tvShowId) {
        if (tvShowId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isTVShowFav;
        Cursor cursor = database.query(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, null, DatabaseHelper.TV_SHOW_ID + " = " + tvShowId, null, null, null, null);
        if (cursor.getCount() == 1)
            isTVShowFav = true;
        else
            isTVShowFav = false;

        cursor.close();
        database.close();
        return isTVShowFav;
    }

    public static List<Integer> getFavTVShowIds(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Integer> favTVShowIds = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            favTVShowIds.add(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TV_SHOW_ID)));
        }
        cursor.close();
        database.close();
        return favTVShowIds;
    }

}
