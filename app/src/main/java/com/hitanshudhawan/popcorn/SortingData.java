package com.hitanshudhawan.popcorn;

import android.util.Log;

import com.hitanshudhawan.popcorn.network.movies.MovieBrief;

import java.util.Collections;
import java.util.List;

/**
 * Created by soumyajit on 24/10/17.
 */

public class SortingData  {

    public static List<MovieBrief> sortAccordingToPopularityMovie(List<MovieBrief> movieBriefArrayList){
        Collections.sort(movieBriefArrayList,MovieBrief.popularitySorting);
        for(MovieBrief m : movieBriefArrayList)
            Log.i("SortingData",m + "\n\n");
        return movieBriefArrayList;
    }

    public static List<MovieBrief> sortAccordingToRatingMovie(List<MovieBrief> movieBriefArrayList){
        Collections.sort(movieBriefArrayList,MovieBrief.ratingSorting);
        for(MovieBrief m : movieBriefArrayList)
            Log.i("SortingData",m + "\n\n");
        return movieBriefArrayList;
    }
}
