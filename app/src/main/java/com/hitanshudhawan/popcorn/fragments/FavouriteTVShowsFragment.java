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
import com.hitanshudhawan.popcorn.adapters.TVShowsSmallAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.tvshows.TVShow;
import com.hitanshudhawan.popcorn.utils.Favourite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 13/8/17.
 */

public class FavouriteTVShowsFragment extends Fragment {

    private ProgressBar mProgressBar;

    private RecyclerView mFavTVShowsRecyclerView;
    private List<TVShow> mFavTVShows;
    private TVShowsSmallAdapter mFavTVShowsAdapter;

    private LinearLayout mEmptyLayout;

    private List<Call<TVShow>> mTVShowDetailsCalls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_tv_shows, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mFavTVShowsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav_tv_shows);
        mFavTVShows = new ArrayList<>();
        mFavTVShowsAdapter = new TVShowsSmallAdapter(getContext(), mFavTVShows);
        mFavTVShowsRecyclerView.setAdapter(mFavTVShowsAdapter);
        mFavTVShowsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layout_recycler_view_fav_tv_shows_empty);

        loadFavTVShows();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO (feature or a bug? :P)
        // for now when coming back to this activity after removing from fav it shows border heart.
        mFavTVShowsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTVShowDetailsCalls != null) {
            for (Call<TVShow> tvShowCall : mTVShowDetailsCalls) {
                if (tvShowCall != null) tvShowCall.cancel();
            }
        }
    }

    private void loadFavTVShows() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        List<Integer> favTVShowIds = Favourite.getFavTVShowIds(getContext());
        if (favTVShowIds.isEmpty()) {
            mProgressBar.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        mTVShowDetailsCalls = new ArrayList<>();
        for (int i = 0; i < favTVShowIds.size(); i++) {
            final int index = i;
            mTVShowDetailsCalls.add(apiService.getTVShowDetails(favTVShowIds.get(index), getResources().getString(R.string.MOVIE_DB_API_KEY)));
            mTVShowDetailsCalls.get(index).enqueue(new Callback<TVShow>() {
                @Override
                public void onResponse(Call<TVShow> call, Response<TVShow> response) {
                    if (!response.isSuccessful()) {
                        mTVShowDetailsCalls.set(index, call.clone());
                        mTVShowDetailsCalls.get(index).enqueue(this);
                        return;
                    }

                    if (response.body() == null) return;

                    mProgressBar.setVisibility(View.GONE);
                    mFavTVShows.add(response.body());
                    mFavTVShowsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<TVShow> call, Throwable t) {

                }
            });
        }
    }

}
