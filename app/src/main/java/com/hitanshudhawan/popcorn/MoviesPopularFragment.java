package com.hitanshudhawan.popcorn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.Movie;
import com.hitanshudhawan.popcorn.network.PopularMovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 27/7/17.
 */

public class MoviesPopularFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_popular, container, false);

        // TESTING
        String APIKEY = "";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PopularMovieResponse> call = apiService.getPopularMovies(APIKEY,1);
        call.enqueue(new Callback<PopularMovieResponse>() {
            @Override
            public void onResponse(Call<PopularMovieResponse> call, Response<PopularMovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                for(int i=0;i<movies.size();i++)
                    Log.d("TAGG",movies.get(i).getTitle());
            }

            @Override
            public void onFailure(Call<PopularMovieResponse> call, Throwable t) {

            }
        });
        // TESTING


        return view;
    }

}
