package com.hitanshudhawan.popcorn.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.SearchResultsAdapter;
import com.hitanshudhawan.popcorn.network.search.SearchAsyncTaskLoader;
import com.hitanshudhawan.popcorn.network.search.SearchResponse;
import com.hitanshudhawan.popcorn.network.search.SearchResult;
import com.hitanshudhawan.popcorn.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class SearchActivity extends AppCompatActivity {

    private String mQuery;

    private SmoothProgressBar mSmoothProgressBar;
    private RecyclerView mSearchResultsRecyclerView;
    private List<SearchResult> mSearchResults;
    private SearchResultsAdapter mSearchResultsAdapter;

    private TextView mEmptyTextView;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        mQuery = receivedIntent.getStringExtra(Constants.QUERY);

        if (mQuery == null || mQuery.trim().isEmpty()) finish();

        setTitle(mQuery);

        mSmoothProgressBar = (SmoothProgressBar) findViewById(R.id.smooth_progress_bar);
        mEmptyTextView = (TextView) findViewById(R.id.text_view_empty_search);

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

    private void loadSearchResults() {
        if (pagesOver) return;

        mSmoothProgressBar.progressiveStart();

        getLoaderManager().initLoader(presentPage, null, new LoaderManager.LoaderCallbacks<SearchResponse>() {

            @Override
            public Loader<SearchResponse> onCreateLoader(int i, Bundle bundle) {
                return new SearchAsyncTaskLoader(SearchActivity.this, mQuery, String.valueOf(presentPage));
            }

            @Override
            public void onLoadFinished(Loader<SearchResponse> loader, SearchResponse searchResponse) {

                if (searchResponse == null) return;
                if (searchResponse.getResults() == null) return;

                mSmoothProgressBar.progressiveStop();
                for (SearchResult searchResult : searchResponse.getResults()) {
                    if (searchResult != null)
                        mSearchResults.add(searchResult);
                }
                mSearchResultsAdapter.notifyDataSetChanged();
                if (mSearchResults.isEmpty()) mEmptyTextView.setVisibility(View.VISIBLE);
                if (searchResponse.getPage() == searchResponse.getTotalPages())
                    pagesOver = true;
                else
                    presentPage++;

            }

            @Override
            public void onLoaderReset(Loader<SearchResponse> loader) {

            }
        }).forceLoad();

    }

}
