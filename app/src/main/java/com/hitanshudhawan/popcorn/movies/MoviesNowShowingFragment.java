package com.hitanshudhawan.popcorn.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.movies.adapters.MoviesAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.Movie;
import com.hitanshudhawan.popcorn.network.NowShowingMovieResponse;
import com.hitanshudhawan.popcorn.network.PopularMovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 27/7/17.
 */

public class MoviesNowShowingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Movie> mMovies;
    private MoviesAdapter mMoviesAdapter;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_now_showing, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_now_showing);
        mMovies = new ArrayList<>();
        mMoviesAdapter = new MoviesAdapter(getContext(), mMovies, new MoviesAdapter.OnBindViewHolderListener() {
            @Override
            public void onBindViewHolder(MoviesAdapter.MoviesViewHolder holder, int position) {
                if (position % mMoviesAdapter.getLargeViewInterval() == 0) {
                    holder.movieTitleTextView.setText(mMovies.get(position).getTitle());
                    Glide.with(getContext()).load("https://image.tmdb.org/t/p/w780/" + mMovies.get(position).getBackdropPath()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.movieImageView);
                } else {
                    holder.movieTitleTextView.setText(mMovies.get(position).getTitle());
                    Glide.with(getContext()).load("https://image.tmdb.org/t/p/w342/" + mMovies.get(position).getPosterPath()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.movieImageView);
                }
            }
        });
        mMoviesAdapter.setLargeViewIntervalEnabled(true);
        mMoviesAdapter.setLargeViewInterval(3);
        mRecyclerView.setAdapter(mMoviesAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % mMoviesAdapter.getLargeViewInterval() == 0) return 2;
                else return 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadMovies();
                    loading = true;
                }
            }
        });

        loadMovies();

        return view;
    }

    private void loadMovies() {
        if(pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NowShowingMovieResponse> call = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage, "US");
        Log.d("TAGG",presentPage+"");
        call.enqueue(new Callback<NowShowingMovieResponse>() {
            @Override
            public void onResponse(Call<NowShowingMovieResponse> call, Response<NowShowingMovieResponse> response) {
                if (response.code() != 200) return;
                mMovies.addAll(response.body().getResults());
                mMoviesAdapter.notifyDataSetChanged();
                if(response.body().getPage() == response.body().getTotalPages())
                    pagesOver = true;
                else
                    presentPage++;
            }

            @Override
            public void onFailure(Call<NowShowingMovieResponse> call, Throwable t) {

            }
        });

    }
}
