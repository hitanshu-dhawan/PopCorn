package com.hitanshudhawan.popcorn.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.fragments.FavouriteMoviesFragment;
import com.hitanshudhawan.popcorn.fragments.FavouritePeopleFragment;
import com.hitanshudhawan.popcorn.fragments.FavouriteTVShowsFragment;

/**
 * Created by hitanshu on 10/8/17.
 *
 * Modified by Angelo on 19/04/19
 */

public class FavouritesPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public FavouritesPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FavouriteMoviesFragment();
            case 1:
                return new FavouriteTVShowsFragment();
            case 2:
                return new FavouritePeopleFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.movies);
            case 1:
                return mContext.getResources().getString(R.string.tv_shows);
            case 2:
                return mContext.getResources().getString(R.string.people);

        }
        return null;
    }
}
