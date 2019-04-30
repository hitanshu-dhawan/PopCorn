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

import com.hitanshudhawan.popcorn.adapters.PeopleBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopularResult;
import com.hitanshudhawan.popcorn.utils.Favourite;

import java.util.ArrayList;
import java.util.List;

import com.hitanshudhawan.popcorn.R;

/**
 * Create by ANGELO on 19/04/19.
 */

public class FavouritePeopleFragment extends Fragment {

    private RecyclerView mFavPeopleRecyclerView;
    //private List<PeopleBrief> mFavPeople;
    //OR with Parcelable
    private List<PersonPopularResult> mFavPeople;
    private PeopleBriefsSmallAdapter mFavPeopleAdapter;

    private LinearLayout mEmptyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_people, container, false);

        mFavPeopleRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav_people);
        mFavPeople = new ArrayList<>();
        mFavPeopleAdapter = new PeopleBriefsSmallAdapter(getContext(), mFavPeople);
        mFavPeopleRecyclerView.setAdapter(mFavPeopleAdapter);
        mFavPeopleRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layout_recycler_view_fav_people_empty);

        loadFavPeople();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO (feature or a bug? :P)
        // for now when coming back to this activity after removing from fav it shows border heart.
        mFavPeopleAdapter.notifyDataSetChanged();
    }

    private void loadFavPeople() {
        List<PersonPopularResult> favPeopleBriefs = Favourite.getFavPeopleBriefs(getContext());
        if (favPeopleBriefs.isEmpty()) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        }

//        for (PeopleBrief peopleBrief : favPeopleBriefs) {
//            mFavPeople.add(peopleBrief);
//        }
        //OR with Parcelable
        for (PersonPopularResult peopleBrief : favPeopleBriefs) {
            mFavPeople.add(peopleBrief);
        }
        mFavPeopleAdapter.notifyDataSetChanged();
    }

}
