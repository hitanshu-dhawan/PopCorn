package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.MovieCastsOfPersonAdapter;
import com.hitanshudhawan.popcorn.adapters.TVCastsOfPersonAdapter;
import com.hitanshudhawan.popcorn.broadcastreceivers.ConnectivityBroadcastReceiver;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.MovieCastOfPerson;
import com.hitanshudhawan.popcorn.network.movies.MovieCastsOfPersonResponse;
import com.hitanshudhawan.popcorn.network.people.Person;
import com.hitanshudhawan.popcorn.network.tvshows.TVCastOfPerson;
import com.hitanshudhawan.popcorn.network.tvshows.TVCastsOfPersonResponse;
import com.hitanshudhawan.popcorn.utils.Constants;
import com.hitanshudhawan.popcorn.utils.NetworkConnection;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonDetailActivity extends AppCompatActivity {

    private int mPersonId;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private CardView mCastImageCardView;
    private int mCastImageSideSize;
    private ImageView mCastImageView;
    private AVLoadingIndicatorView mProgressBar;
    private TextView mCastNameTextView;
    private TextView mCastAgeTextView;
    private TextView mCastBirthPlaceTextView;
    private ImageButton mBackImageButton;

    private TextView mCastBioHeaderTextView;
    private TextView mCastBioTextView;
    private TextView mCastReadMoreBioTextView;

    private TextView mMovieCastTextView;
    private RecyclerView mMovieCastRecyclerView;
    private List<MovieCastOfPerson> mMovieCastOfPersons;
    private MovieCastsOfPersonAdapter mMovieCastsOfPersonAdapter;

    private TextView mTVCastTextView;
    private RecyclerView mTVCastRecyclerView;
    private List<TVCastOfPerson> mTVCastOfPersons;
    private TVCastsOfPersonAdapter mTVCastsOfPersonAdapter;

    private Snackbar mConnectivitySnackbar;
    private ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private boolean isBroadcastReceiverRegistered;
    private boolean isActivityLoaded;
    private Call<Person> mPersonDetailsCall;
    private Call<MovieCastsOfPersonResponse> mMovieCastsOfPersonsCall;
    private Call<TVCastsOfPersonResponse> mTVCastsOfPersonsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setTitle("");

        Intent receivedIntent = getIntent();
        mPersonId = receivedIntent.getIntExtra(Constants.PERSON_ID, -1);

        if (mPersonId == -1) finish();

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        mCastImageCardView = (CardView) findViewById(R.id.card_view_cast_detail);
        mCastImageSideSize = (int) (getResources().getDisplayMetrics().widthPixels * 0.33);
        mCastImageCardView.getLayoutParams().height = mCastImageSideSize;
        mCastImageCardView.getLayoutParams().width = mCastImageSideSize;
        mCastImageCardView.setRadius(mCastImageSideSize / 2);
        mCastImageView = (ImageView) findViewById(R.id.image_view_cast_detail);
        mProgressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar_cast_detail);
        mProgressBar.setVisibility(View.GONE);
        mCastNameTextView = (TextView) findViewById(R.id.text_view_name_cast_detail);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mCastNameTextView.getLayoutParams();
        params.setMargins(params.leftMargin, mCastImageSideSize / 2, params.rightMargin, params.bottomMargin);
        mCastAgeTextView = (TextView) findViewById(R.id.text_view_age_cast_detail);
        mCastBirthPlaceTextView = (TextView) findViewById(R.id.text_view_birthplace_cast_detail);

        mBackImageButton = (ImageButton) findViewById(R.id.image_button_back_cast_detail);
        mBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mCastBioHeaderTextView = (TextView) findViewById(R.id.text_view_bio_header_person_detail);
        mCastBioTextView = (TextView) findViewById(R.id.text_view_bio_person_detail);
        mCastReadMoreBioTextView = (TextView) findViewById(R.id.text_view_read_more_person_detail);

        mMovieCastTextView = (TextView) findViewById(R.id.text_view_movie_cast_person_detail);
        mMovieCastRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movie_cast_person_detail);
        mMovieCastOfPersons = new ArrayList<>();
        mMovieCastsOfPersonAdapter = new MovieCastsOfPersonAdapter(PersonDetailActivity.this, mMovieCastOfPersons);
        mMovieCastRecyclerView.setAdapter(mMovieCastsOfPersonAdapter);
        mMovieCastRecyclerView.setLayoutManager(new LinearLayoutManager(PersonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mTVCastTextView = (TextView) findViewById(R.id.text_view_tv_cast_person_detail);
        mTVCastRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tv_cast_person_detail);
        mTVCastOfPersons = new ArrayList<>();
        mTVCastsOfPersonAdapter = new TVCastsOfPersonAdapter(PersonDetailActivity.this, mTVCastOfPersons);
        mTVCastRecyclerView.setAdapter(mTVCastsOfPersonAdapter);
        mTVCastRecyclerView.setLayoutManager(new LinearLayoutManager(PersonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        if (NetworkConnection.isConnected(PersonDetailActivity.this)) {
            isActivityLoaded = true;
            loadActivity();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isActivityLoaded && !NetworkConnection.isConnected(PersonDetailActivity.this)) {
            mConnectivitySnackbar = Snackbar.make(mCastNameTextView, R.string.no_network, Snackbar.LENGTH_INDEFINITE);
            mConnectivitySnackbar.show();
            mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver(new ConnectivityBroadcastReceiver.ConnectivityReceiverListener() {
                @Override
                public void onNetworkConnectionConnected() {
                    mConnectivitySnackbar.dismiss();
                    isActivityLoaded = true;
                    loadActivity();
                    isBroadcastReceiverRegistered = false;
                    unregisterReceiver(mConnectivityBroadcastReceiver);
                }
            });
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadcastReceiverRegistered = true;
            registerReceiver(mConnectivityBroadcastReceiver, intentFilter);
        } else if (!isActivityLoaded && NetworkConnection.isConnected(PersonDetailActivity.this)) {
            isActivityLoaded = true;
            loadActivity();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isBroadcastReceiverRegistered) {
            isBroadcastReceiverRegistered = false;
            unregisterReceiver(mConnectivityBroadcastReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPersonDetailsCall != null) mPersonDetailsCall.cancel();
        if (mMovieCastsOfPersonsCall != null) mMovieCastsOfPersonsCall.cancel();
        if (mTVCastsOfPersonsCall != null) mTVCastsOfPersonsCall.cancel();
    }

    private void loadActivity() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        mProgressBar.setVisibility(View.VISIBLE);

        mPersonDetailsCall = apiService.getPersonDetails(mPersonId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        mPersonDetailsCall.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, final Response<Person> response) {
                if (!response.isSuccessful()) {
                    mPersonDetailsCall = call.clone();
                    mPersonDetailsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;

                mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                            if (response.body().getName() != null)
                                mCollapsingToolbarLayout.setTitle(response.body().getName());
                            else
                                mCollapsingToolbarLayout.setTitle("");
                            mToolbar.setVisibility(View.VISIBLE);
                        } else {
                            mCollapsingToolbarLayout.setTitle("");
                            mToolbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                Glide.with(getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_1280 + response.body().getProfilePath())
                        .asBitmap()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(mCastImageView);

                if (response.body().getName() != null)
                    mCastNameTextView.setText(response.body().getName());
                else
                    mCastNameTextView.setText("");

                setAge(response.body().getDateOfBirth());

                if (response.body().getPlaceOfBirth() != null && !response.body().getPlaceOfBirth().trim().isEmpty())
                    mCastBirthPlaceTextView.setText(response.body().getPlaceOfBirth());

                if (response.body().getBiography() != null && !response.body().getBiography().trim().isEmpty()) {
                    mCastBioHeaderTextView.setVisibility(View.VISIBLE);
                    mCastReadMoreBioTextView.setVisibility(View.VISIBLE);
                    mCastBioTextView.setText(response.body().getBiography());
                    mCastReadMoreBioTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCastBioTextView.setMaxLines(Integer.MAX_VALUE);
                            mCastReadMoreBioTextView.setVisibility(View.GONE);
                        }
                    });
                }

                setMovieCast(response.body().getId());

                setTVShowCast(response.body().getId());
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {

            }
        });
    }

    private void setAge(String dateOfBirthString) {
        if (dateOfBirthString != null && !dateOfBirthString.trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(dateOfBirthString);
                mCastAgeTextView.setText((Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(sdf2.format(releaseDate))) + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMovieCast(Integer personId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mMovieCastsOfPersonsCall = apiService.getMovieCastsOfPerson(personId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        mMovieCastsOfPersonsCall.enqueue(new Callback<MovieCastsOfPersonResponse>() {
            @Override
            public void onResponse(Call<MovieCastsOfPersonResponse> call, Response<MovieCastsOfPersonResponse> response) {
                if (!response.isSuccessful()) {
                    mMovieCastsOfPersonsCall = call.clone();
                    mMovieCastsOfPersonsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getCasts() == null) return;

                for (MovieCastOfPerson movieCastOfPerson : response.body().getCasts()) {
                    if (movieCastOfPerson == null) return;
                    if (movieCastOfPerson.getTitle() != null && movieCastOfPerson.getPosterPath() != null) {
                        mMovieCastTextView.setVisibility(View.VISIBLE);
                        mMovieCastOfPersons.add(movieCastOfPerson);
                    }
                }
                mMovieCastsOfPersonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieCastsOfPersonResponse> call, Throwable t) {

            }
        });
    }

    private void setTVShowCast(Integer personId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mTVCastsOfPersonsCall = apiService.getTVCastsOfPerson(personId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        mTVCastsOfPersonsCall.enqueue(new Callback<TVCastsOfPersonResponse>() {
            @Override
            public void onResponse(Call<TVCastsOfPersonResponse> call, Response<TVCastsOfPersonResponse> response) {
                if (!response.isSuccessful()) {
                    mTVCastsOfPersonsCall = call.clone();
                    mTVCastsOfPersonsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getCasts() == null) return;

                for (TVCastOfPerson tvCastOfPerson : response.body().getCasts()) {
                    if (tvCastOfPerson == null) return;
                    if (tvCastOfPerson.getName() != null && tvCastOfPerson.getPosterPath() != null) {
                        mTVCastTextView.setVisibility(View.VISIBLE);
                        mTVCastOfPersons.add(tvCastOfPerson);
                    }
                }
                mTVCastsOfPersonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TVCastsOfPersonResponse> call, Throwable t) {

            }
        });
    }

}
