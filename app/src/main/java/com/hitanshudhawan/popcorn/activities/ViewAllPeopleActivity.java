package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hitanshudhawan.popcorn.adapters.PeopleBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopular;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopularResult;

import java.util.ArrayList;
import java.util.List;

import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;

import com.hitanshudhawan.popcorn.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create by ANGELO on 19/04/19.
 */

public class ViewAllPeopleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    //private List<PeopleBrief> mPeople;
    //OR with Parcelable
    private List<PersonPopularResult> mPeople;

    private PeopleBriefsSmallAdapter mPeopleAdapter;

    private int mPeopleType;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private Call<PersonPopular> mPopularPeopleResponseShowsCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hitanshudhawan.popcorn.R.layout.activity_view_all_people);
        Toolbar toolbar = (Toolbar) findViewById(com.hitanshudhawan.popcorn.R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        mPeopleType = receivedIntent.getIntExtra(Constants.VIEW_ALL_PEOPLE_TYPE, -1);

        if (mPeopleType == -1) finish();

        switch (mPeopleType) {
            case Constants.POPULAR_PEOPLE_TYPE:
                setTitle(com.hitanshudhawan.popcorn.R.string.popular_response_people);
                break;

        }

        mRecyclerView = (RecyclerView) findViewById(com.hitanshudhawan.popcorn.R.id.recycler_view_view_all);
        mPeople = new ArrayList<>();
        mPeopleAdapter = new PeopleBriefsSmallAdapter(ViewAllPeopleActivity.this, mPeople);
        mRecyclerView.setAdapter(mPeopleAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(com.hitanshudhawan.popcorn.activities.ViewAllPeopleActivity.this, 3);
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
                    loadPeople(mPeopleType);
                    loading = true;
                }

            }
        });

        loadPeople(mPeopleType);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mPeopleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPopularPeopleResponseShowsCall != null) mPopularPeopleResponseShowsCall.cancel();

    }

    private void loadPeople(int tvShowType) {
        if (pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        switch (tvShowType) {
            //PopularPeopleResponse  <=>  PersonPopular
            case Constants.POPULAR_PEOPLE_TYPE:
                mPopularPeopleResponseShowsCall = apiService.getPopularPeopleWithParcelable(getResources().getString(com.hitanshudhawan.popcorn.R.string.MOVIE_DB_API_KEY), presentPage);
                mPopularPeopleResponseShowsCall.enqueue(new Callback<PersonPopular>() {
                    @Override
                    public void onResponse(Call<PersonPopular> call, Response<PersonPopular> response) {
                        if (!response.isSuccessful()) {
                            mPopularPeopleResponseShowsCall = call.clone();
                            mPopularPeopleResponseShowsCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        for (PeopleBrief people : response.body().getResults()) {
//                            if (people != null && people.getΝame() != null && people.getProfilePath() != null)
//                                mPeople.add(people);
//                        }
                        //OR with Parcelable
                        for (PersonPopularResult people : response.body().getResults()) {
                            if (people != null && people.getName() != null && people.getProfilePath() != null)
                                mPeople.add(people);
                        }
                        mPeopleAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<PersonPopular> call, Throwable t) {

                    }
                });
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
