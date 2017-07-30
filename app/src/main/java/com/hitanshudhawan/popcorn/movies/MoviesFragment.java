package com.hitanshudhawan.popcorn.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.Resource;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;
import com.hitanshudhawan.popcorn.network.movies.NowShowingMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.PopularMovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 30/7/17.
 */

public class MoviesFragment extends Fragment {

    RecyclerView mNowShowingRecyclerView;
    List<MovieBrief> mNowShowingMovies;
    MoviesBigAdapter mNowShowingAdapter;

    RecyclerView mPopularRecyclerView;
    List<MovieBrief> mPopularMovies;
    MoviesSmallAdapter mPopularAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mNowShowingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_now_showing);
        (new LinearSnapHelper()).attachToRecyclerView(mNowShowingRecyclerView);
        mPopularRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_popular);

        mNowShowingMovies = new ArrayList<>();
        mPopularMovies = new ArrayList<>();

        mNowShowingAdapter = new MoviesBigAdapter(getContext(),mNowShowingMovies);
        mPopularAdapter = new MoviesSmallAdapter(getContext(),mPopularMovies);

        mNowShowingRecyclerView.setAdapter(mNowShowingAdapter);
        mNowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        loadNowShowingMovies();

        mPopularRecyclerView.setAdapter(mPopularAdapter);
        mPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        loadPopularMovies();

        return view;
    }

    private void loadNowShowingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NowShowingMovieResponse> call = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<NowShowingMovieResponse>() {
            @Override
            public void onResponse(Call<NowShowingMovieResponse> call, Response<NowShowingMovieResponse> response) {
                if(response.code() != 200) return;
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getBackdropPath() != null)
                        mNowShowingMovies.add(movieBrief);
                }
                mNowShowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NowShowingMovieResponse> call, Throwable t) {

            }
        });
    }

    private void loadPopularMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PopularMovieResponse> call = apiService.getPopularMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<PopularMovieResponse>() {
            @Override
            public void onResponse(Call<PopularMovieResponse> call, Response<PopularMovieResponse> response) {
                if(response.code() != 200) return;
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getPosterPath() != null)
                        mPopularMovies.add(movieBrief);
                }
                mPopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularMovieResponse> call, Throwable t) {

            }
        });
    }
}
