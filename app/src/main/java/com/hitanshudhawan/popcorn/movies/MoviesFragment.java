package com.hitanshudhawan.popcorn.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hitanshudhawan.popcorn.MainActivity;
import com.hitanshudhawan.popcorn.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by hitanshu on 27/7/17.
 */

public class MoviesFragment extends Fragment {

    ViewPager mMoviesViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mMoviesViewPager = (ViewPager) view.findViewById(R.id.movies_view_pager);
        mMoviesViewPager.setAdapter(new MoviesPagerAdapter(getChildFragmentManager()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SmartTabLayout tabLayout = (SmartTabLayout) ((MainActivity)getActivity()).findViewById(R.id.tab_layout);
        tabLayout.setViewPager(mMoviesViewPager);
    }

    public class MoviesPagerAdapter extends FragmentPagerAdapter {

        public MoviesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MoviesHighLightsFragment();
                case 1:
                    return new MoviesNowShowingFragment();
                case 2:
                    return new MoviesUpcomingFragment();
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
                    return getResources().getString(R.string.highlights);
                case 1:
                    return getResources().getString(R.string.now_showing);
                case 2:
                    return getResources().getString(R.string.upcoming);
            }
            return null;
        }
    }

}

