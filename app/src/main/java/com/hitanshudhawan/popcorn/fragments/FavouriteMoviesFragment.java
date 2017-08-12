package com.hitanshudhawan.popcorn.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.MoviesSmallAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.Movie;
import com.hitanshudhawan.popcorn.utils.Favourite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 10/8/17.
 */

public class FavouriteMoviesFragment extends Fragment {

    private ProgressBar mProgressBar;

    private RecyclerView mFavMoviesRecyclerView;
    private List<Movie> mFavMovies;
    private MoviesSmallAdapter mFavMoviesAdapter;

    private LinearLayout mEmptyLayout;

    private List<Call<Movie>> mMovieDetailsCalls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_movies, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mFavMoviesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav_movies);
        mFavMovies = new ArrayList<>();
        mFavMoviesAdapter = new MoviesSmallAdapter(getContext(), mFavMovies);
        mFavMoviesRecyclerView.setAdapter(mFavMoviesAdapter);
        mFavMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layout_recycler_view_fav_movies_empty);

        loadFavMovies();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO (feature or a bug? :P)
        // for now when coming back to this activity after removing from fav it shows border heart.
        mFavMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMovieDetailsCalls != null) {
            for (Call<Movie> movieCall : mMovieDetailsCalls) {
                if (movieCall != null) movieCall.cancel();
            }
        }
    }

    private void loadFavMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        List<Integer> favMovieIds = Favourite.getFavMovieIds(getContext());
        if (favMovieIds.isEmpty()) {
            mProgressBar.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        mMovieDetailsCalls = new ArrayList<>();
        for (int i = 0; i < favMovieIds.size(); i++) {
            final int index = i;
            mMovieDetailsCalls.add(apiService.getMovieDetails(favMovieIds.get(index), getResources().getString(R.string.MOVIE_DB_API_KEY)));
            mMovieDetailsCalls.get(index).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (!response.isSuccessful()) {
                        mMovieDetailsCalls.set(index, call.clone());
                        mMovieDetailsCalls.get(index).enqueue(this);
                        return;
                    }

                    if (response.body() == null) return;

                    mProgressBar.setVisibility(View.GONE);
                    mFavMovies.add(response.body());
                    mFavMoviesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
        }
    }


}
