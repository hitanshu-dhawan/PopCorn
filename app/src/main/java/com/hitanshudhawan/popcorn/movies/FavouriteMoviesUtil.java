package com.hitanshudhawan.popcorn.movies;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hitanshu on 31/7/17.
 */

public class FavouriteMoviesUtil {

    private static final String SHARED_PREFERENCES_FILE_NAME = "FavouriteMovies";
    private static final String SHARED_PREFERENCES_KEY = "favouriteMovies";

    public static void addMovie(Context context, Integer movieId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        Set<String> favouriteMovies = sharedPreferences.getStringSet(SHARED_PREFERENCES_KEY,null);
        HashSet<String> newFavouriteMovies = null;
        if (favouriteMovies != null) {
            newFavouriteMovies = new HashSet<>(favouriteMovies);
            newFavouriteMovies.add(movieId.toString());
        }
        else {
            newFavouriteMovies = new HashSet<>();
            newFavouriteMovies.add(movieId.toString());
        }
        sharedPreferencesEditor.putStringSet(SHARED_PREFERENCES_KEY,newFavouriteMovies);
        sharedPreferencesEditor.apply();
    }

    public static void removeMovie(Context context, Integer movieId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        Set<String> favouriteMovies = sharedPreferences.getStringSet(SHARED_PREFERENCES_KEY,null);
        HashSet<String> newFavouriteMovies = null;
        if (favouriteMovies != null) {
            newFavouriteMovies = new HashSet<>(favouriteMovies);
            favouriteMovies.remove(movieId.toString());
        }
        sharedPreferencesEditor.putStringSet(SHARED_PREFERENCES_KEY,newFavouriteMovies);
        sharedPreferencesEditor.apply();
    }

    public static boolean isFavouriteMovie(Context context, Integer movieId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        HashSet<String> favouriteMovies = (HashSet<String>) sharedPreferences.getStringSet(SHARED_PREFERENCES_KEY,null);
        if (favouriteMovies != null)
            return favouriteMovies.contains(movieId.toString());
        return false;
    }
}
