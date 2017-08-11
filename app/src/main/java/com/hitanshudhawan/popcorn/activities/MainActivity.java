package com.hitanshudhawan.popcorn.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.fragments.FavouritesFragment;
import com.hitanshudhawan.popcorn.fragments.MoviesFragment;
import com.hitanshudhawan.popcorn.utils.Constant;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MoviesFragment mMoviesFragment;
    FavouritesFragment mFavouritesFragment;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(MainActivity.this);

        mMoviesFragment = new MoviesFragment();
        mFavouritesFragment = new FavouritesFragment();

        mNavigationView.setCheckedItem(R.id.nav_movies);
        setTitle(R.string.movies);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_activity_fragment_container, mMoviesFragment, Constant.TAG_MOVIES_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (id) {
            case R.id.nav_movies:
                setTitle(R.string.movies);
                if (fragmentManager.findFragmentByTag(Constant.TAG_MOVIES_FRAGMENT) == null) {
                    fragmentTransaction.add(R.id.main_activity_fragment_container, mMoviesFragment, Constant.TAG_MOVIES_FRAGMENT);
                }
                if (fragmentManager.findFragmentByTag(Constant.TAG_FAV_FRAGMENT) != null) {
                    fragmentTransaction.hide(mFavouritesFragment);
                }
                fragmentTransaction.show(mMoviesFragment);
                fragmentTransaction.commit();
                return true;
            case R.id.nav_tv_shows:
                setTitle(R.string.tv_shows);
                return true;
            case R.id.nav_favorites:
                setTitle(R.string.favorites);
                if (fragmentManager.findFragmentByTag(Constant.TAG_FAV_FRAGMENT) == null) {
                    fragmentTransaction.add(R.id.main_activity_fragment_container, mFavouritesFragment, Constant.TAG_FAV_FRAGMENT);
                }
                if (fragmentManager.findFragmentByTag(Constant.TAG_MOVIES_FRAGMENT) != null) {
                    fragmentTransaction.hide(mMoviesFragment);
                }
                fragmentTransaction.show(mFavouritesFragment);
                fragmentTransaction.commit();
                return true;
            case R.id.nav_settings:
                return false;
            case R.id.nav_about:
                return false;
        }

        return false;
    }
}
