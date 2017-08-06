package com.hitanshudhawan.popcorn.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.MoviesViewAllAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;
import com.hitanshudhawan.popcorn.network.movies.NowShowingMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllMoviesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<MovieBrief> mMovies;
    private MoviesViewAllAdapter mMoviesAdapter;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_view_all);
        mMovies = new ArrayList<>();
        mMoviesAdapter = new MoviesViewAllAdapter(ViewAllMoviesActivity.this, mMovies);
        mRecyclerView.setAdapter(mMoviesAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAllMoviesActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

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

        loadMovies(); // todo send which movies to load .... now showing,upcoming etc.

    }

    private void loadMovies() {
        if(pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NowShowingMoviesResponse> call = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage, "US");
        call.enqueue(new Callback<NowShowingMoviesResponse>() {
            @Override
            public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                if (response.code() != 200) return;
                mMovies.addAll(response.body().getResults());
                mMoviesAdapter.notifyDataSetChanged();
                if(response.body().getPage() == response.body().getTotalPages())
                    pagesOver = true;
                else
                    presentPage++;
            }

            @Override
            public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

            }
        });
    }

}
