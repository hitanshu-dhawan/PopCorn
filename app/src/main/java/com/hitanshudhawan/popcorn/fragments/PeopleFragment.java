package com.hitanshudhawan.popcorn.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.activities.ViewAllPeopleActivity;
import com.hitanshudhawan.popcorn.adapters.PeopleBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.broadcastreceivers.ConnectivityBroadcastReceiver;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopular;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopularResult;
import com.hitanshudhawan.popcorn.utils.Constants;
import com.hitanshudhawan.popcorn.utils.NetworkConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create by ANGELO on 19/04/19.
 */

/**
 * Fragment to show popular persons of the day with continuous scrolling.
 * On click opens Detail activity for an actor.
 */

public class PeopleFragment extends Fragment {
    private ProgressBar mProgressBar;

    //People
    private boolean mPopularSectionLoadedPeople;

    //People
    private FrameLayout mPopularLayoutPeople;
    private TextView mPopularViewAllTextViewPeople;
    private RecyclerView mPopularRecyclerViewPeople;
    private List<PersonPopularResult> mPopularPeople;
    private PeopleBriefsSmallAdapter mPopularAdapterPeople;

    private Snackbar mConnectivitySnackbar;
    private ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private boolean isBroadcastReceiverRegistered;
    private boolean isFragmentLoaded;

    //People
    //private Call<GenresListPeople> mGenresListCallPeople;
    private Call<PersonPopular> mGenresListCallPeople;

    //People
    private Call<PersonPopular> mPopularPeopleCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        //People
        mPopularSectionLoadedPeople = false;

        //People
        mPopularLayoutPeople = (FrameLayout) view.findViewById(R.id.layout_popular_people);

        mPopularViewAllTextViewPeople = (TextView) view.findViewById(R.id.text_view_view_all_popular_people);

        mPopularRecyclerViewPeople = (RecyclerView) view.findViewById(R.id.recycler_view_popular_people);
        //...end

        //People
        mPopularPeople = new ArrayList<PersonPopularResult>();

        mPopularAdapterPeople = new PeopleBriefsSmallAdapter(getContext(), mPopularPeople);

        mPopularRecyclerViewPeople.setAdapter(mPopularAdapterPeople);
        mPopularRecyclerViewPeople.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //People....end

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

        //People
        //if (mGenresListCallPeople != null) mGenresListCallPeople.cancel();
        if (mPopularPeopleCall != null) mPopularPeopleCall.cancel();

    }



    private void loadFragment() {
            //People
            loadPopularPeople();
            //mProgressBar.setVisibility(View.VISIBLE);
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
                //PeopleBrief
//                for (PeopleBrief people_actor : response.body().getResults()) {
//                    if (people_actor != null && people_actor.getProfilePath() != null)
//                        mPopularPeople.add(people_actor);
//                }
                //PersonPopularResult
                for (PersonPopularResult people_actor : response.body().getResults()) {
                    if (people_actor != null && people_actor.getProfilePath() != null)
                        mPopularPeople.add(people_actor);
                }
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
        if (mPopularSectionLoadedPeople) {
            mProgressBar.setVisibility(View.GONE);

            //People
            mPopularLayoutPeople.setVisibility(View.VISIBLE);
            mPopularRecyclerViewPeople.setVisibility(View.VISIBLE);

        }
    }

}