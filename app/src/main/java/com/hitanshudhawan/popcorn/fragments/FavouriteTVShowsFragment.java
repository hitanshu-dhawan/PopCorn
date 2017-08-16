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

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.TVShowBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.network.tvshows.TVShowBrief;
import com.hitanshudhawan.popcorn.utils.Favourite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitanshu on 13/8/17.
 */

public class FavouriteTVShowsFragment extends Fragment {

    private RecyclerView mFavTVShowsRecyclerView;
    private List<TVShowBrief> mFavTVShows;
    private TVShowBriefsSmallAdapter mFavTVShowsAdapter;

    private LinearLayout mEmptyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_tv_shows, container, false);

        mFavTVShowsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav_tv_shows);
        mFavTVShows = new ArrayList<>();
        mFavTVShowsAdapter = new TVShowBriefsSmallAdapter(getContext(), mFavTVShows);
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

    private void loadFavTVShows() {
        List<TVShowBrief> favTVShowBriefs = Favourite.getFavTVShowBriefs(getContext());
        if (favTVShowBriefs.isEmpty()) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        }

        for (TVShowBrief tvShowBrief : favTVShowBriefs) {
            mFavTVShows.add(tvShowBrief);
        }
        mFavTVShowsAdapter.notifyDataSetChanged();
    }

}
