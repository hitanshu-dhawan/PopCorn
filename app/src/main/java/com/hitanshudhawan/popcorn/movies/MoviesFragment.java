package com.hitanshudhawan.popcorn.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.MoviesBigAdapter;
import com.hitanshudhawan.popcorn.adapters.MoviesSmallAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;
import com.hitanshudhawan.popcorn.network.movies.NowShowingMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.PopularMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.TopRatedMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.UpcomingMovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 30/7/17.
 */

public class MoviesFragment extends Fragment {

    FrameLayout mNowShowingLayout;
    RecyclerView mNowShowingRecyclerView;
    List<MovieBrief> mNowShowingMovies;
    MoviesBigAdapter mNowShowingAdapter;

    FrameLayout mPopularLayout;
    RecyclerView mPopularRecyclerView;
    List<MovieBrief> mPopularMovies;
    MoviesSmallAdapter mPopularAdapter;

    FrameLayout mUpcomingLayout;
    RecyclerView mUpcomingRecyclerView;
    List<MovieBrief> mUpcomingMovies;
    MoviesBigAdapter mUpcomingAdapter;

    FrameLayout mTopRatedLayout;
    RecyclerView mTopRatedRecyclerView;
    List<MovieBrief> mTopRatedMovies;
    MoviesSmallAdapter mTopRatedAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mNowShowingLayout = (FrameLayout) view.findViewById(R.id.layout_now_showing);
        mPopularLayout = (FrameLayout) view.findViewById(R.id.layout_popular);
        mUpcomingLayout = (FrameLayout) view.findViewById(R.id.layout_upcoming);
        mTopRatedLayout = (FrameLayout) view.findViewById(R.id.layout_top_rated);

        mNowShowingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_now_showing);
        (new LinearSnapHelper()).attachToRecyclerView(mNowShowingRecyclerView);
        mPopularRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_popular);
        mUpcomingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_upcoming);
        (new LinearSnapHelper()).attachToRecyclerView(mUpcomingRecyclerView);
        mTopRatedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_top_rated);

        mNowShowingMovies = new ArrayList<>();
        mPopularMovies = new ArrayList<>();
        mUpcomingMovies = new ArrayList<>();
        mTopRatedMovies = new ArrayList<>();

        mNowShowingAdapter = new MoviesBigAdapter(getContext(),mNowShowingMovies);
        mPopularAdapter = new MoviesSmallAdapter(getContext(),mPopularMovies);
        mUpcomingAdapter = new MoviesBigAdapter(getContext(),mUpcomingMovies);
        mTopRatedAdapter = new MoviesSmallAdapter(getContext(),mTopRatedMovies);

        mNowShowingRecyclerView.setAdapter(mNowShowingAdapter);
        mNowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        loadNowShowingMovies();

        mPopularRecyclerView.setAdapter(mPopularAdapter);
        mPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        loadPopularMovies();

        mUpcomingRecyclerView.setAdapter(mUpcomingAdapter);
        mUpcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        loadUpcomingMovies();

        mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
        mTopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        loadTopRatedMovies();

        return view;
    }

    private void loadNowShowingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NowShowingMovieResponse> call = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<NowShowingMovieResponse>() {
            @Override
            public void onResponse(Call<NowShowingMovieResponse> call, Response<NowShowingMovieResponse> response) {
                if(response.code() != 200) return;
                mNowShowingLayout.setVisibility(View.VISIBLE);
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
                mPopularLayout.setVisibility(View.VISIBLE);
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

    private void loadUpcomingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UpcomingMovieResponse> call = apiService.getUpcomingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<UpcomingMovieResponse>() {
            @Override
            public void onResponse(Call<UpcomingMovieResponse> call, Response<UpcomingMovieResponse> response) {
                if(response.code() != 200) return;
                mUpcomingLayout.setVisibility(View.VISIBLE);
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getBackdropPath() != null)
                        mUpcomingMovies.add(movieBrief);
                }
                mUpcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UpcomingMovieResponse> call, Throwable t) {

            }
        });
    }

    private void loadTopRatedMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TopRatedMovieResponse> call = apiService.getTopRatedMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<TopRatedMovieResponse>() {
            @Override
            public void onResponse(Call<TopRatedMovieResponse> call, Response<TopRatedMovieResponse> response) {
                if(response.code() != 200) return;
                mTopRatedLayout.setVisibility(View.VISIBLE);
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getPosterPath() != null)
                        mTopRatedMovies.add(movieBrief);
                }
                mTopRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopRatedMovieResponse> call, Throwable t) {

            }
        });
    }
}
