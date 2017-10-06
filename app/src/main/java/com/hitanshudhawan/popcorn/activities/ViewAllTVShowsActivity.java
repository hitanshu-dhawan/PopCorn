package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.TVShowBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.tvshows.AiringTodayTVShowsResponse;
import com.hitanshudhawan.popcorn.network.tvshows.OnTheAirTVShowsResponse;
import com.hitanshudhawan.popcorn.network.tvshows.PopularTVShowsResponse;
import com.hitanshudhawan.popcorn.network.tvshows.TVShowBrief;
import com.hitanshudhawan.popcorn.network.tvshows.TopRatedTVShowsResponse;
import com.hitanshudhawan.popcorn.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllTVShowsActivity extends AppCompatActivity {

    private SmoothProgressBar mSmoothProgressBar;

    private RecyclerView mRecyclerView;
    private List<TVShowBrief> mTVShows;
    private TVShowBriefsSmallAdapter mTVShowsAdapter;

    private int mTVShowType;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private Call<AiringTodayTVShowsResponse> mAiringTodayTVShowsCall;
    private Call<OnTheAirTVShowsResponse> mOnTheAirTVShowsCall;
    private Call<PopularTVShowsResponse> mPopularTVShowsCall;
    private Call<TopRatedTVShowsResponse> mTopRatedTVShowsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tvshows);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar toolActionBar = getSupportActionBar();
        if(toolActionBar!=null)
            toolActionBar.setDisplayHomeAsUpEnabled(true);
        Intent receivedIntent = getIntent();
        mTVShowType = receivedIntent.getIntExtra(Constant.VIEW_ALL_TV_SHOWS_TYPE, -1);

        if (mTVShowType == -1) finish();

        switch (mTVShowType) {
            case Constant.AIRING_TODAY_TV_SHOWS_TYPE:
                setTitle(R.string.airing_today_tv_shows);
                break;
            case Constant.ON_THE_AIR_TV_SHOWS_TYPE:
                setTitle(R.string.on_the_air_tv_shows);
                break;
            case Constant.POPULAR_TV_SHOWS_TYPE:
                setTitle(R.string.popular_tv_shows);
                break;
            case Constant.TOP_RATED_TV_SHOWS_TYPE:
                setTitle(R.string.top_rated_tv_shows);
                break;
        }

        mSmoothProgressBar = (SmoothProgressBar) findViewById(R.id.smooth_progress_bar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_view_all);
        mTVShows = new ArrayList<>();
        mTVShowsAdapter = new TVShowBriefsSmallAdapter(ViewAllTVShowsActivity.this, mTVShows);
        mRecyclerView.setAdapter(mTVShowsAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(ViewAllTVShowsActivity.this, 3);
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
                    loadTVShows(mTVShowType);
                    loading = true;
                }

            }
        });

        loadTVShows(mTVShowType);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mTVShowsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAiringTodayTVShowsCall != null) mAiringTodayTVShowsCall.cancel();
        if (mOnTheAirTVShowsCall != null) mOnTheAirTVShowsCall.cancel();
        if (mPopularTVShowsCall != null) mPopularTVShowsCall.cancel();
        if (mTopRatedTVShowsCall != null) mTopRatedTVShowsCall.cancel();
    }

    private void loadTVShows(int tvShowType) {
        if (pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mSmoothProgressBar.progressiveStart();

        switch (tvShowType) {
            case Constant.AIRING_TODAY_TV_SHOWS_TYPE:
                mAiringTodayTVShowsCall = apiService.getAiringTodayTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage);
                mAiringTodayTVShowsCall.enqueue(new Callback<AiringTodayTVShowsResponse>() {
                    @Override
                    public void onResponse(Call<AiringTodayTVShowsResponse> call, Response<AiringTodayTVShowsResponse> response) {
                        if (!response.isSuccessful()) {
                            mAiringTodayTVShowsCall = call.clone();
                            mAiringTodayTVShowsCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

                        mSmoothProgressBar.progressiveStop();
                        for (TVShowBrief tvShowBrief : response.body().getResults()) {
                            if (tvShowBrief != null && tvShowBrief.getName() != null && tvShowBrief.getPosterPath() != null)
                                mTVShows.add(tvShowBrief);
                        }
                        mTVShowsAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<AiringTodayTVShowsResponse> call, Throwable t) {

                    }
                });
                break;
            case Constant.ON_THE_AIR_TV_SHOWS_TYPE:
                mOnTheAirTVShowsCall = apiService.getOnTheAirTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage);
                mOnTheAirTVShowsCall.enqueue(new Callback<OnTheAirTVShowsResponse>() {
                    @Override
                    public void onResponse(Call<OnTheAirTVShowsResponse> call, Response<OnTheAirTVShowsResponse> response) {
                        if (!response.isSuccessful()) {
                            mOnTheAirTVShowsCall = call.clone();
                            mOnTheAirTVShowsCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

                        mSmoothProgressBar.progressiveStop();
                        for (TVShowBrief tvShowBrief : response.body().getResults()) {
                            if (tvShowBrief != null && tvShowBrief.getName() != null && tvShowBrief.getPosterPath() != null)
                                mTVShows.add(tvShowBrief);
                        }
                        mTVShowsAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<OnTheAirTVShowsResponse> call, Throwable t) {

                    }
                });
                break;
            case Constant.POPULAR_TV_SHOWS_TYPE:
                mPopularTVShowsCall = apiService.getPopularTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage);
                mPopularTVShowsCall.enqueue(new Callback<PopularTVShowsResponse>() {
                    @Override
                    public void onResponse(Call<PopularTVShowsResponse> call, Response<PopularTVShowsResponse> response) {
                        if (!response.isSuccessful()) {
                            mPopularTVShowsCall = call.clone();
                            mPopularTVShowsCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

                        mSmoothProgressBar.progressiveStop();
                        for (TVShowBrief tvShowBrief : response.body().getResults()) {
                            if (tvShowBrief != null && tvShowBrief.getName() != null && tvShowBrief.getPosterPath() != null)
                                mTVShows.add(tvShowBrief);
                        }
                        mTVShowsAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<PopularTVShowsResponse> call, Throwable t) {

                    }
                });
                break;
            case Constant.TOP_RATED_TV_SHOWS_TYPE:
                mTopRatedTVShowsCall = apiService.getTopRatedTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage);
                mTopRatedTVShowsCall.enqueue(new Callback<TopRatedTVShowsResponse>() {
                    @Override
                    public void onResponse(Call<TopRatedTVShowsResponse> call, Response<TopRatedTVShowsResponse> response) {
                        if (!response.isSuccessful()) {
                            mTopRatedTVShowsCall = call.clone();
                            mTopRatedTVShowsCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

                        mSmoothProgressBar.progressiveStop();
                        for (TVShowBrief tvShowBrief : response.body().getResults()) {
                            if (tvShowBrief != null && tvShowBrief.getName() != null && tvShowBrief.getPosterPath() != null)
                                mTVShows.add(tvShowBrief);
                        }
                        mTVShowsAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<TopRatedTVShowsResponse> call, Throwable t) {

                    }
                });
                break;
        }
    }

}
