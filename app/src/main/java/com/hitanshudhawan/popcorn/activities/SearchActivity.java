package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.SearchResultsAdapter;
import com.hitanshudhawan.popcorn.network.search.SearchAsyncTask;
import com.hitanshudhawan.popcorn.network.search.SearchResponse;
import com.hitanshudhawan.popcorn.network.search.SearchResult;
import com.hitanshudhawan.popcorn.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class SearchActivity extends AppCompatActivity {

    private String mQuery;

    private SmoothProgressBar mSmoothProgressBar;
    private RecyclerView mSearchResultsRecyclerView;
    private List<SearchResult> mSearchResults;
    private SearchResultsAdapter mSearchResultsAdapter;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private SearchAsyncTask mSearchAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        mQuery = receivedIntent.getStringExtra(Constant.QUERY);

        if (mQuery == null || mQuery.trim().isEmpty()) finish();

        setTitle(mQuery);

        mSmoothProgressBar = (SmoothProgressBar) findViewById(R.id.smooth_progress_bar);

        mSearchResultsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);
        mSearchResults = new ArrayList<>();
        mSearchResultsAdapter = new SearchResultsAdapter(SearchActivity.this, mSearchResults);
        mSearchResultsRecyclerView.setAdapter(mSearchResultsAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
        mSearchResultsRecyclerView.setLayoutManager(linearLayoutManager);
        mSearchResultsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    loadSearchResults();
                    loading = true;
                }

            }
        });

        loadSearchResults();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchAsyncTask != null) mSearchAsyncTask.cancel(true);
    }

    private void loadSearchResults() {
        if (pagesOver) return;

        mSmoothProgressBar.progressiveStart();

        mSearchAsyncTask = new SearchAsyncTask(SearchActivity.this, new SearchAsyncTask.OnSearchDoneListener() {
            @Override
            public void onSearchDone(SearchResponse searchResponse) {

                if (searchResponse == null) return;
                if (searchResponse.getResults() == null) return;

                mSmoothProgressBar.progressiveStop();
                for (SearchResult searchResult : searchResponse.getResults()) {
                    if (searchResult != null)
                        mSearchResults.add(searchResult);
                }
                mSearchResultsAdapter.notifyDataSetChanged();
                if (searchResponse.getPage() == searchResponse.getTotalPages())
                    pagesOver = true;
                else
                    presentPage++;

            }
        });
        mSearchAsyncTask.execute(mQuery, String.valueOf(presentPage));

    }

}
