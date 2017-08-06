package com.hitanshudhawan.popcorn.fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.activities.ViewAllMoviesActivity;
import com.hitanshudhawan.popcorn.adapters.MoviesLargeAdapter;
import com.hitanshudhawan.popcorn.adapters.MoviesSmallAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;
import com.hitanshudhawan.popcorn.network.movies.NowShowingMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.PopularMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.TopRatedMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.UpcomingMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 30/7/17.
 */

public class MoviesFragment extends Fragment {

    private FrameLayout mNowShowingLayout;
    private TextView mNowShowingViewAllTextView;
    private RecyclerView mNowShowingRecyclerView;
    private List<MovieBrief> mNowShowingMovies;
    private MoviesLargeAdapter mNowShowingAdapter;

    private FrameLayout mPopularLayout;
    private TextView mPopularViewAllTextView;
    private RecyclerView mPopularRecyclerView;
    private List<MovieBrief> mPopularMovies;
    private MoviesSmallAdapter mPopularAdapter;

    private FrameLayout mUpcomingLayout;
    private TextView mUpcomingViewAllTextView;
    private RecyclerView mUpcomingRecyclerView;
    private List<MovieBrief> mUpcomingMovies;
    private MoviesLargeAdapter mUpcomingAdapter;

    private FrameLayout mTopRatedLayout;
    private TextView mTopRatedViewAllTextView;
    private RecyclerView mTopRatedRecyclerView;
    private List<MovieBrief> mTopRatedMovies;
    private MoviesSmallAdapter mTopRatedAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mNowShowingLayout = (FrameLayout) view.findViewById(R.id.layout_now_showing);
        mPopularLayout = (FrameLayout) view.findViewById(R.id.layout_popular);
        mUpcomingLayout = (FrameLayout) view.findViewById(R.id.layout_upcoming);
        mTopRatedLayout = (FrameLayout) view.findViewById(R.id.layout_top_rated);

        mNowShowingViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_now_showing);
        mPopularViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_popular);
        mUpcomingViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_upcoming);
        mTopRatedViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_top_rated);

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

        mNowShowingAdapter = new MoviesLargeAdapter(getContext(),mNowShowingMovies);
        mPopularAdapter = new MoviesSmallAdapter(getContext(),mPopularMovies);
        mUpcomingAdapter = new MoviesLargeAdapter(getContext(),mUpcomingMovies);
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

        mNowShowingViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                startActivity(intent); // todo send nnowshowing in intent...
            }
        });
        mPopularViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mUpcomingViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mTopRatedViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void loadNowShowingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NowShowingMoviesResponse> call = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<NowShowingMoviesResponse>() {
            @Override
            public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                if(response.code() != 200) return;
                mNowShowingLayout.setVisibility(View.VISIBLE);
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getBackdropPath() != null)
                        mNowShowingMovies.add(movieBrief);
                }
                mNowShowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadPopularMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PopularMoviesResponse> call = apiService.getPopularMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                if(response.code() != 200) return;
                mPopularLayout.setVisibility(View.VISIBLE);
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getPosterPath() != null)
                        mPopularMovies.add(movieBrief);
                }
                mPopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadUpcomingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UpcomingMoviesResponse> call = apiService.getUpcomingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<UpcomingMoviesResponse>() {
            @Override
            public void onResponse(Call<UpcomingMoviesResponse> call, Response<UpcomingMoviesResponse> response) {
                if(response.code() != 200) return;
                mUpcomingLayout.setVisibility(View.VISIBLE);
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getBackdropPath() != null)
                        mUpcomingMovies.add(movieBrief);
                }
                mUpcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UpcomingMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadTopRatedMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TopRatedMoviesResponse> call = apiService.getTopRatedMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        call.enqueue(new Callback<TopRatedMoviesResponse>() {
            @Override
            public void onResponse(Call<TopRatedMoviesResponse> call, Response<TopRatedMoviesResponse> response) {
                if(response.code() != 200) return;
                mTopRatedLayout.setVisibility(View.VISIBLE);
                for(MovieBrief movieBrief : response.body().getResults()) {
                    if(movieBrief.getPosterPath() != null)
                        mTopRatedMovies.add(movieBrief);
                }
                mTopRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopRatedMoviesResponse> call, Throwable t) {

            }
        });
    }
}
