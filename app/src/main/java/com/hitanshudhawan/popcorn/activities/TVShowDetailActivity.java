package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.adapters.TVShowBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.adapters.TVShowCastAdapter;
import com.hitanshudhawan.popcorn.adapters.VideoAdapter;
import com.hitanshudhawan.popcorn.broadcastreceivers.ConnectivityBroadcastReceiver;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.tvshows.Genre;
import com.hitanshudhawan.popcorn.network.tvshows.Network;
import com.hitanshudhawan.popcorn.network.tvshows.SimilarTVShowsResponse;
import com.hitanshudhawan.popcorn.network.tvshows.TVShow;
import com.hitanshudhawan.popcorn.network.tvshows.TVShowBrief;
import com.hitanshudhawan.popcorn.network.tvshows.TVShowCastBrief;
import com.hitanshudhawan.popcorn.network.tvshows.TVShowCreditsResponse;
import com.hitanshudhawan.popcorn.network.videos.Video;
import com.hitanshudhawan.popcorn.network.videos.VideosResponse;
import com.hitanshudhawan.popcorn.utils.Constant;
import com.hitanshudhawan.popcorn.utils.Favourite;
import com.hitanshudhawan.popcorn.utils.NetworkConnection;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailActivity extends AppCompatActivity {

    private int mTVShowId;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private ConstraintLayout mTVShowTabLayout;
    private ImageView mPosterImageView;
    private int mPosterHeight;
    private int mPosterWidth;
    private AVLoadingIndicatorView mPosterProgressBar;
    private ImageView mBackdropImageView;
    private int mBackdropHeight;
    private int mBackdropWidth;
    private AVLoadingIndicatorView mBackdropProgressBar;
    private TextView mTitleTextView;
    private TextView mGenreTextView;
    private TextView mYearTextView;
    private ImageButton mFavImageButton;
    private ImageButton mShareImageButton;

    private TextView mOverviewTextView;
    private TextView mOverviewReadMoreTextView;
    private LinearLayout mDetailsLayout;
    private TextView mDetailsTextView;

    private TextView mVideosTextView;
    private RecyclerView mVideosRecyclerView;
    private List<Video> mVideos;
    private VideoAdapter mVideosAdapter;

    private View mHorizontalLine;

    private TextView mCastTextView;
    private RecyclerView mCastRecyclerView;
    private List<TVShowCastBrief> mCasts;
    private TVShowCastAdapter mCastAdapter;

    private TextView mSimilarTVShowsTextView;
    private RecyclerView mSimilarTVShowsRecyclerView;
    private List<TVShowBrief> mSimilarTVShows;
    private TVShowBriefsSmallAdapter mSimilarTVShowsAdapter;

    private Snackbar mConnectivitySnackbar;
    private ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private boolean isBroadcastReceiverRegistered;
    private boolean isActivityLoaded;
    private Call<TVShow> mTVShowDetailsCall;
    private Call<VideosResponse> mVideosCall;
    private Call<TVShowCreditsResponse> mTVShowCreditsCall;
    private Call<SimilarTVShowsResponse> mSimilarTVShowsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setTitle("");

        Intent receivedIntent = getIntent();
        mTVShowId = receivedIntent.getIntExtra(Constant.TV_SHOW_ID, -1);

        if (mTVShowId == -1) finish();

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        mPosterWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.25);
        mPosterHeight = (int) (mPosterWidth / 0.66);
        mBackdropWidth = getResources().getDisplayMetrics().widthPixels;
        mBackdropHeight = (int) (mBackdropWidth / 1.77);

        mTVShowTabLayout = (ConstraintLayout) findViewById(R.id.layout_toolbar_tv_show);
        mTVShowTabLayout.getLayoutParams().height = mBackdropHeight + (int) (mPosterHeight * 0.9);

        mPosterImageView = (ImageView) findViewById(R.id.image_view_poster);
        mPosterImageView.getLayoutParams().width = mPosterWidth;
        mPosterImageView.getLayoutParams().height = mPosterHeight;
        mPosterProgressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar_poster);
        mPosterProgressBar.setVisibility(View.GONE);

        mBackdropImageView = (ImageView) findViewById(R.id.image_view_backdrop);
        mBackdropImageView.getLayoutParams().height = mBackdropHeight;
        mBackdropProgressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar_backdrop);
        mBackdropProgressBar.setVisibility(View.GONE);

        mTitleTextView = (TextView) findViewById(R.id.text_view_title_tv_show_detail);
        mGenreTextView = (TextView) findViewById(R.id.text_view_genre_tv_show_detail);
        mYearTextView = (TextView) findViewById(R.id.text_view_year_tv_show_detail);

        mFavImageButton = (ImageButton) findViewById(R.id.image_button_fav_tv_show_detail);
        mShareImageButton = (ImageButton) findViewById(R.id.image_button_share_tv_show_detail);

        mOverviewTextView = (TextView) findViewById(R.id.text_view_overview_tv_show_detail);
        mOverviewReadMoreTextView = (TextView) findViewById(R.id.text_view_read_more_tv_show_detail);
        mDetailsLayout = (LinearLayout) findViewById(R.id.layout_details_tv_show_detail);
        mDetailsTextView = (TextView) findViewById(R.id.text_view_details_tv_show_detail);

        mVideosTextView = (TextView) findViewById(R.id.text_view_trailer_tv_show_detail);
        mVideosRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_trailers_tv_show_detail);
        (new LinearSnapHelper()).attachToRecyclerView(mVideosRecyclerView);
        mVideos = new ArrayList<>();
        mVideosAdapter = new VideoAdapter(TVShowDetailActivity.this, mVideos);
        mVideosRecyclerView.setAdapter(mVideosAdapter);
        mVideosRecyclerView.setLayoutManager(new LinearLayoutManager(TVShowDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mHorizontalLine = (View) findViewById(R.id.view_horizontal_line);

        mCastTextView = (TextView) findViewById(R.id.text_view_cast_tv_show_detail);
        mCastRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_cast_tv_show_detail);
        mCasts = new ArrayList<>();
        mCastAdapter = new TVShowCastAdapter(TVShowDetailActivity.this, mCasts);
        mCastRecyclerView.setAdapter(mCastAdapter);
        mCastRecyclerView.setLayoutManager(new LinearLayoutManager(TVShowDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mSimilarTVShowsTextView = (TextView) findViewById(R.id.text_view_similar_tv_show_detail);
        mSimilarTVShowsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_similar_tv_show_detail);
        mSimilarTVShows = new ArrayList<>();
        mSimilarTVShowsAdapter = new TVShowBriefsSmallAdapter(TVShowDetailActivity.this, mSimilarTVShows);
        mSimilarTVShowsRecyclerView.setAdapter(mSimilarTVShowsAdapter);
        mSimilarTVShowsRecyclerView.setLayoutManager(new LinearLayoutManager(TVShowDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        if (NetworkConnection.isConnected(TVShowDetailActivity.this)) {
            isActivityLoaded = true;
            loadActivity();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mSimilarTVShowsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isActivityLoaded && !NetworkConnection.isConnected(TVShowDetailActivity.this)) {
            mConnectivitySnackbar = Snackbar.make(mTitleTextView, R.string.no_network, Snackbar.LENGTH_INDEFINITE);
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
        } else if (!isActivityLoaded && NetworkConnection.isConnected(TVShowDetailActivity.this)) {
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

        if (mTVShowDetailsCall != null) mTVShowDetailsCall.cancel();
        if (mVideosCall != null) mVideosCall.cancel();
        if (mTVShowCreditsCall != null) mTVShowCreditsCall.cancel();
        if (mSimilarTVShowsCall != null) mSimilarTVShowsCall.cancel();
    }

    private void loadActivity() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        mPosterProgressBar.setVisibility(View.VISIBLE);
        mBackdropProgressBar.setVisibility(View.VISIBLE);

        mTVShowDetailsCall = apiService.getTVShowDetails(mTVShowId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        mTVShowDetailsCall.enqueue(new Callback<TVShow>() {
            @Override
            public void onResponse(Call<TVShow> call, final Response<TVShow> response) {
                if (!response.isSuccessful()) {
                    mTVShowDetailsCall = call.clone();
                    mTVShowDetailsCall.enqueue(this);
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

                Glide.with(getApplicationContext()).load(Constant.IMAGE_LOADING_BASE_URL_1000 + response.body().getPosterPath())
                        .asBitmap()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                mPosterProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mPosterProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(mPosterImageView);

                Glide.with(getApplicationContext()).load(Constant.IMAGE_LOADING_BASE_URL_1000 + response.body().getBackdropPath())
                        .asBitmap()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                mBackdropProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mBackdropProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(mBackdropImageView);

                if (response.body().getName() != null)
                    mTitleTextView.setText(response.body().getName());
                else
                    mTitleTextView.setText("");

                setGenres(response.body().getGenres());

                setYear(response.body().getFirstAirDate());

                mFavImageButton.setVisibility(View.VISIBLE);
                mShareImageButton.setVisibility(View.VISIBLE);
                setImageButtons(response.body().getId(), response.body().getPosterPath(), response.body().getName(), response.body().getHomepage());

                if (response.body().getOverview() != null) {
                    mOverviewReadMoreTextView.setVisibility(View.VISIBLE);
                    mOverviewTextView.setText(response.body().getOverview());
                    mOverviewReadMoreTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOverviewTextView.setMaxLines(Integer.MAX_VALUE);
                            mDetailsLayout.setVisibility(View.VISIBLE);
                            mOverviewReadMoreTextView.setVisibility(View.GONE);
                        }
                    });
                } else {
                    mOverviewTextView.setText("");
                }

                setDetails(response.body().getFirstAirDate(), response.body().getEpisodeRunTime(), response.body().getStatus(), response.body().getOriginCountries(), response.body().getNetworks());

                setVideos();

                mHorizontalLine.setVisibility(View.VISIBLE);

                setCasts();

                setSimilarTVShows();
            }

            @Override
            public void onFailure(Call<TVShow> call, Throwable t) {

            }
        });
    }

    private void setGenres(List<Genre> genresList) {
        String genres = "";
        if (genresList != null) {
            for (int i = 0; i < genresList.size(); i++) {
                if (genresList.get(i) == null) continue;
                if (i == genresList.size() - 1) {
                    genres = genres.concat(genresList.get(i).getGenreName());
                } else {
                    genres = genres.concat(genresList.get(i).getGenreName() + ", ");
                }
            }
        }
        mGenreTextView.setText(genres);
    }

    private void setYear(String firstAirDateString) {
        if (firstAirDateString != null && !firstAirDateString.trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date firstAirDate = sdf1.parse(firstAirDateString);
                mYearTextView.setText(sdf2.format(firstAirDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            mYearTextView.setText("");
        }
    }

    private void setImageButtons(final Integer tvShowId, final String posterPath, final String tvShowName, final String homepage) {
        if (tvShowId == null) return;
        if (Favourite.isTVShowFav(TVShowDetailActivity.this, tvShowId)) {
            mFavImageButton.setTag(Constant.TAG_FAV);
            mFavImageButton.setImageResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            mFavImageButton.setTag(Constant.TAG_NOT_FAV);
            mFavImageButton.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        mFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if ((int) mFavImageButton.getTag() == Constant.TAG_FAV) {
                    Favourite.removeTVShowFromFav(TVShowDetailActivity.this, tvShowId);
                    mFavImageButton.setTag(Constant.TAG_NOT_FAV);
                    mFavImageButton.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
                } else {
                    Favourite.addTVShowToFav(TVShowDetailActivity.this, tvShowId, posterPath, tvShowName);
                    mFavImageButton.setTag(Constant.TAG_FAV);
                    mFavImageButton.setImageResource(R.mipmap.ic_favorite_white_24dp);
                }
            }
        });
        mShareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent movieShareIntent = new Intent(Intent.ACTION_SEND);
                movieShareIntent.setType("text/plain");
                String extraText = "";
                if (tvShowName != null) extraText += tvShowName + "\n";
                if (homepage != null) extraText += homepage;
                movieShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
                startActivity(movieShareIntent);

            }
        });
    }

    private void setDetails(String firstAirDateString, List<Integer> runtime, String status, List<String> originCountries, List<Network> networks) {
        String detailsString = "";

        if (firstAirDateString != null && !firstAirDateString.trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy");
            try {
                Date releaseDate = sdf1.parse(firstAirDateString);
                detailsString += sdf2.format(releaseDate) + "\n";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            detailsString = "-\n";
        }

        if (runtime != null && !runtime.isEmpty() && runtime.get(0) != 0) {
            if (runtime.get(0) < 60) {
                detailsString += runtime.get(0) + " min(s)" + "\n";
            } else {
                detailsString += runtime.get(0) / 60 + " hr " + runtime.get(0) % 60 + " mins" + "\n";
            }
        } else {
            detailsString += "-\n";
        }

        if (status != null && !status.trim().isEmpty()) {
            detailsString += status + "\n";
        } else {
            detailsString += "-\n";
        }

        String originCountriesString = "";
        if (originCountries != null && !originCountries.isEmpty()) {
            for (String country : originCountries) {
                if (country == null || country.trim().isEmpty()) continue;
                originCountriesString += country + ", ";
            }
            if (!originCountriesString.isEmpty())
                detailsString += originCountriesString.substring(0, originCountriesString.length() - 2) + "\n";
            else
                detailsString += "-\n";
        } else {
            detailsString += "-\n";
        }

        String networksString = "";
        if (networks != null && !networks.isEmpty()) {
            for (Network network : networks) {
                if (network == null || network.getName() == null || network.getName().isEmpty())
                    continue;
                networksString += network.getName() + ", ";
            }
            if (!networksString.isEmpty())
                detailsString += networksString.substring(0, networksString.length() - 2);
            else
                detailsString += "-\n";
        } else {
            detailsString += "-\n";
        }

        mDetailsTextView.setText(detailsString);
    }

    private void setVideos() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mVideosCall = apiService.getTVShowVideos(mTVShowId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        mVideosCall.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (!response.isSuccessful()) {
                    mVideosCall = call.clone();
                    mVideosCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getVideos() == null) return;

                for (Video video : response.body().getVideos()) {
                    if (video != null && video.getSite() != null && video.getSite().equals("YouTube") && video.getType() != null && video.getType().equals("Trailer"))
                        mVideos.add(video);
                }
                if (!mVideos.isEmpty())
                    mVideosTextView.setVisibility(View.VISIBLE);
                mVideosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {

            }
        });
    }

    private void setCasts() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mTVShowCreditsCall = apiService.getTVShowCredits(mTVShowId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        mTVShowCreditsCall.enqueue(new Callback<TVShowCreditsResponse>() {
            @Override
            public void onResponse(Call<TVShowCreditsResponse> call, Response<TVShowCreditsResponse> response) {
                if (!response.isSuccessful()) {
                    mTVShowCreditsCall = call.clone();
                    mTVShowCreditsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getCasts() == null) return;

                for (TVShowCastBrief castBrief : response.body().getCasts()) {
                    if (castBrief != null && castBrief.getName() != null)
                        mCasts.add(castBrief);
                }

                if (!mCasts.isEmpty())
                    mCastTextView.setVisibility(View.VISIBLE);
                mCastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TVShowCreditsResponse> call, Throwable t) {

            }
        });
    }

    private void setSimilarTVShows() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mSimilarTVShowsCall = apiService.getSimilarTVShows(mTVShowId, getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        mSimilarTVShowsCall.enqueue(new Callback<SimilarTVShowsResponse>() {
            @Override
            public void onResponse(Call<SimilarTVShowsResponse> call, Response<SimilarTVShowsResponse> response) {
                if (!response.isSuccessful()) {
                    mSimilarTVShowsCall = call.clone();
                    mSimilarTVShowsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (TVShowBrief tvShowBrief : response.body().getResults()) {
                    if (tvShowBrief != null && tvShowBrief.getName() != null && tvShowBrief.getPosterPath() != null)
                        mSimilarTVShows.add(tvShowBrief);
                }

                if (!mSimilarTVShows.isEmpty())
                    mSimilarTVShowsTextView.setVisibility(View.VISIBLE);
                mSimilarTVShowsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SimilarTVShowsResponse> call, Throwable t) {

            }
        });
    }

}
