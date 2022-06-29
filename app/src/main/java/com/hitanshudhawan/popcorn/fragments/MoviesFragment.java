package com.hitanshudhawan.popcorn.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.activities.ViewAllMoviesActivity;
import com.hitanshudhawan.popcorn.activities.ViewAllPeopleActivity;
import com.hitanshudhawan.popcorn.adapters.MovieBriefsLargeAdapter;
import com.hitanshudhawan.popcorn.adapters.MovieBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.adapters.PeopleBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.broadcastreceivers.ConnectivityBroadcastReceiver;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.GenresList;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;
import com.hitanshudhawan.popcorn.network.movies.NowShowingMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.PopularMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.TopRatedMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.UpcomingMoviesResponse;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopular;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopularResult;
import com.hitanshudhawan.popcorn.utils.Constants;
import com.hitanshudhawan.popcorn.utils.MovieGenres;
import com.hitanshudhawan.popcorn.utils.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 30/7/17.
 *
 * Modified by Angelo on 19/04/19
 */

// hitanshu : MoviesFragment and TVShowsFragment are mostly similar
public class MoviesFragment extends Fragment {

    private ProgressBar mProgressBar;
    private boolean mNowShowingSectionLoaded;
    private boolean mPopularSectionLoaded;
    private boolean mUpcomingSectionLoaded;
    private boolean mTopRatedSectionLoaded;

    private FrameLayout mNowShowingLayout;
    private TextView mNowShowingViewAllTextView;
    private RecyclerView mNowShowingRecyclerView;
    private List<MovieBrief> mNowShowingMovies;
    private MovieBriefsLargeAdapter mNowShowingAdapter;

    private FrameLayout mPopularLayout;
    private TextView mPopularViewAllTextView;
    private RecyclerView mPopularRecyclerView;
    private List<MovieBrief> mPopularMovies;
    private MovieBriefsSmallAdapter mPopularAdapter;

    //People
    private boolean mPopularSectionLoadedPeople;

    private FrameLayout mUpcomingLayout;
    private TextView mUpcomingViewAllTextView;
    private RecyclerView mUpcomingRecyclerView;
    private List<MovieBrief> mUpcomingMovies;
    private MovieBriefsLargeAdapter mUpcomingAdapter;

    private FrameLayout mTopRatedLayout;
    private TextView mTopRatedViewAllTextView;
    private RecyclerView mTopRatedRecyclerView;
    private List<MovieBrief> mTopRatedMovies;
    private MovieBriefsSmallAdapter mTopRatedAdapter;

    //People
    private FrameLayout mPopularLayoutPeople;
    private TextView mPopularViewAllTextViewPeople;
    private RecyclerView mPopularRecyclerViewPeople;
    //private List<PeopleBrief> mPopularPeople;
    //OR with Parcelable
    private List<PersonPopularResult> mPopularPeople;
    private PeopleBriefsSmallAdapter mPopularAdapterPeople;

    private Snackbar mConnectivitySnackbar;
    private ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private boolean isBroadcastReceiverRegistered;
    private boolean isFragmentLoaded;
    private Call<GenresList> mGenresListCall;
    private Call<NowShowingMoviesResponse> mNowShowingMoviesCall;
    private Call<PopularMoviesResponse> mPopularMoviesCall;
    private Call<UpcomingMoviesResponse> mUpcomingMoviesCall;
    private Call<TopRatedMoviesResponse> mTopRatedMoviesCall;

    //People
    private Call<PersonPopular> mPopularPeopleCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mNowShowingSectionLoaded = false;
        mPopularSectionLoaded = false;
        mUpcomingSectionLoaded = false;
        mTopRatedSectionLoaded = false;

        mNowShowingLayout = (FrameLayout) view.findViewById(R.id.layout_now_showing);
        mPopularLayout = (FrameLayout) view.findViewById(R.id.layout_popular);
        mUpcomingLayout = (FrameLayout) view.findViewById(R.id.layout_upcoming);
        mTopRatedLayout = (FrameLayout) view.findViewById(R.id.layout_top_rated);

        mNowShowingViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_now_showing);
        mPopularViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_popular);
        mUpcomingViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_upcoming);
        mTopRatedViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_top_rated);

        mNowShowingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_now_showing);
        (new LinearSnapHelper()).attachToRecyclerView(mNowShowingRecyclerView);
        mPopularRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_popular);
        mUpcomingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_upcoming);
        (new LinearSnapHelper()).attachToRecyclerView(mUpcomingRecyclerView);
        mTopRatedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_top_rated);

        //People
        mPopularSectionLoadedPeople = false;

        //People
        mPopularLayoutPeople = (FrameLayout) view.findViewById(R.id.layout_popular_people);

        mPopularViewAllTextViewPeople = (TextView) view.findViewById(R.id.text_view_view_all_popular_people);

        mPopularRecyclerViewPeople = (RecyclerView) view.findViewById(R.id.recycler_view_popular_people);
        //...end

        mNowShowingMovies = new ArrayList<>();
        mPopularMovies = new ArrayList<>();
        mUpcomingMovies = new ArrayList<>();
        mTopRatedMovies = new ArrayList<>();

        mNowShowingAdapter = new MovieBriefsLargeAdapter(getContext(), mNowShowingMovies);
        mPopularAdapter = new MovieBriefsSmallAdapter(getContext(), mPopularMovies);
        mUpcomingAdapter = new MovieBriefsLargeAdapter(getContext(), mUpcomingMovies);
        mTopRatedAdapter = new MovieBriefsSmallAdapter(getContext(), mTopRatedMovies);

        mNowShowingRecyclerView.setAdapter(mNowShowingAdapter);
        mNowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mPopularRecyclerView.setAdapter(mPopularAdapter);
        mPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mUpcomingRecyclerView.setAdapter(mUpcomingAdapter);
        mUpcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
        mTopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //People
        mPopularPeople = new ArrayList<PersonPopularResult>();

        mPopularAdapterPeople = new PeopleBriefsSmallAdapter(getContext(), mPopularPeople);

        mPopularRecyclerViewPeople.setAdapter(mPopularAdapterPeople);
        mPopularRecyclerViewPeople.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //People....end

        mNowShowingViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.NOW_SHOWING_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        mPopularViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.POPULAR_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        mUpcomingViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.UPCOMING_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        mTopRatedViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.TOP_RATED_MOVIES_TYPE);
                startActivity(intent);
            }
        });

        //People
        mPopularViewAllTextViewPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllPeopleActivity.class);
                intent.putExtra(Constants.VIEW_ALL_PEOPLE_TYPE, Constants.POPULAR_PEOPLE_TYPE);
                startActivity(intent);
            }
        });
        //....end

        if (NetworkConnection.isConnected(getContext())) {
            isFragmentLoaded = true;
            loadFragment();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mNowShowingAdapter.notifyDataSetChanged();
        mPopularAdapter.notifyDataSetChanged();
        mUpcomingAdapter.notifyDataSetChanged();
        mTopRatedAdapter.notifyDataSetChanged();

        //People.
        mPopularAdapterPeople.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isFragmentLoaded && !NetworkConnection.isConnected(getContext())) {
            mConnectivitySnackbar = Snackbar.make(getActivity().findViewById(R.id.main_activity_fragment_container), R.string.no_network, Snackbar.LENGTH_INDEFINITE);
            mConnectivitySnackbar.show();
            mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver(new ConnectivityBroadcastReceiver.ConnectivityReceiverListener() {
                @Override
                public void onNetworkConnectionConnected() {
                    mConnectivitySnackbar.dismiss();
                    isFragmentLoaded = true;
                    loadFragment();
                    isBroadcastReceiverRegistered = false;
                    getActivity().unregisterReceiver(mConnectivityBroadcastReceiver);
                }
            });
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadcastReceiverRegistered = true;
            getActivity().registerReceiver(mConnectivityBroadcastReceiver, intentFilter);
        } else if (!isFragmentLoaded && NetworkConnection.isConnected(getContext())) {
            isFragmentLoaded = true;
            loadFragment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isBroadcastReceiverRegistered) {
            mConnectivitySnackbar.dismiss();
            isBroadcastReceiverRegistered = false;
            getActivity().unregisterReceiver(mConnectivityBroadcastReceiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mGenresListCall != null) mGenresListCall.cancel();
        if (mNowShowingMoviesCall != null) mNowShowingMoviesCall.cancel();
        if (mPopularMoviesCall != null) mPopularMoviesCall.cancel();
        if (mUpcomingMoviesCall != null) mUpcomingMoviesCall.cancel();
        if (mTopRatedMoviesCall != null) mTopRatedMoviesCall.cancel();

        //People
        if (mPopularPeopleCall != null) mPopularPeopleCall.cancel();
    }

    private void loadFragment() {

        if (MovieGenres.isGenresListLoaded()) {
            loadNowShowingMovies();
            loadPopularMovies();
            loadUpcomingMovies();
            loadTopRatedMovies();

        } else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            mProgressBar.setVisibility(View.VISIBLE);
            mGenresListCall = apiService.getMovieGenresList(getResources().getString(R.string.MOVIE_DB_API_KEY));
            mGenresListCall.enqueue(new Callback<GenresList>() {
                @Override
                public void onResponse(Call<GenresList> call, Response<GenresList> response) {
                    if (!response.isSuccessful()) {
                        mGenresListCall = call.clone();
                        mGenresListCall.enqueue(this);
                        return;
                    }

                    if (response.body() == null) return;
                    if (response.body().getGenres() == null) return;

                    MovieGenres.loadGenresList(response.body().getGenres());
                    loadNowShowingMovies();
                    loadPopularMovies();
                    loadUpcomingMovies();
                    loadTopRatedMovies();
                }

                @Override
                public void onFailure(Call<GenresList> call, Throwable t) {

                }
            });

            //People
            loadPopularPeople();
            //mProgressBar.setVisibility(View.VISIBLE);

        }

    }

    private void loadNowShowingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mNowShowingMoviesCall = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        mNowShowingMoviesCall.enqueue(new Callback<NowShowingMoviesResponse>() {
            @Override
            public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    mNowShowingMoviesCall = call.clone();
                    mNowShowingMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mNowShowingSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getBackdropPath() != null)
                        mNowShowingMovies.add(movieBrief);
                }
                mNowShowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadPopularMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mPopularMoviesCall = apiService.getPopularMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        mPopularMoviesCall.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    mPopularMoviesCall = call.clone();
                    mPopularMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mPopularSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getPosterPath() != null)
                        mPopularMovies.add(movieBrief);
                }
                mPopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadUpcomingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mUpcomingMoviesCall = apiService.getUpcomingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        mUpcomingMoviesCall.enqueue(new Callback<UpcomingMoviesResponse>() {
            @Override
            public void onResponse(Call<UpcomingMoviesResponse> call, Response<UpcomingMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    mUpcomingMoviesCall = call.clone();
                    mUpcomingMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mUpcomingSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getBackdropPath() != null)
                        mUpcomingMovies.add(movieBrief);
                }
                mUpcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UpcomingMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadTopRatedMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mTopRatedMoviesCall = apiService.getTopRatedMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        mTopRatedMoviesCall.enqueue(new Callback<TopRatedMoviesResponse>() {
            @Override
            public void onResponse(Call<TopRatedMoviesResponse> call, Response<TopRatedMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    mTopRatedMoviesCall = call.clone();
                    mTopRatedMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mTopRatedSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getPosterPath() != null)
                        mTopRatedMovies.add(movieBrief);
                }
                mTopRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopRatedMoviesResponse> call, Throwable t) {

            }
        });
    }

    /**setOnClickListener**/
    /**PeopleBriefsSmallAdapter **/
    private void loadPopularPeople() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mPopularPeopleCall = apiService.getPopularPeopleWithParcelable(getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        mPopularPeopleCall.enqueue(new Callback<PersonPopular>() {
            @Override
            public void onResponse(Call<PersonPopular> call, Response<PersonPopular> response) {
                if (!response.isSuccessful()) {
                    mPopularPeopleCall = call.clone();
                    mPopularPeopleCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getResults() == null) return;
                mPopularSectionLoadedPeople = true;
                checkAllDataLoaded();
//                for (PeopleBrief people_actor : response.body().getResults()) {
//                    if (people_actor != null && people_actor.getProfilePath() != null)
//                        mPopularPeople.add(people_actor);
//                }
                //OR with Parcelable
                for (PersonPopularResult people_actor : response.body().getResults()) {
                    if (people_actor != null && people_actor.getProfilePath() != null)
                        mPopularPeople.add(people_actor);
                }
                /**  Here I have one change. **/
                mPopularAdapterPeople.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<PersonPopular> call, Throwable t) {
            }
        });
    }

    /**setOnClickListener**/
    /**PeopleBriefsSmallAdapter **/
    private void checkAllDataLoaded() {
        if (mNowShowingSectionLoaded && mPopularSectionLoaded && mUpcomingSectionLoaded && mTopRatedSectionLoaded

                && mPopularSectionLoadedPeople
                ) {
            mProgressBar.setVisibility(View.GONE);
            mNowShowingLayout.setVisibility(View.VISIBLE);
            mNowShowingRecyclerView.setVisibility(View.VISIBLE);
            mPopularLayout.setVisibility(View.VISIBLE);
            mPopularRecyclerView.setVisibility(View.VISIBLE);
            mUpcomingLayout.setVisibility(View.VISIBLE);
            mUpcomingRecyclerView.setVisibility(View.VISIBLE);
            mTopRatedLayout.setVisibility(View.VISIBLE);
            mTopRatedRecyclerView.setVisibility(View.VISIBLE);

            //People
            mPopularLayoutPeople.setVisibility(View.VISIBLE);
            mPopularRecyclerViewPeople.setVisibility(View.VISIBLE);
        }
    }

}
