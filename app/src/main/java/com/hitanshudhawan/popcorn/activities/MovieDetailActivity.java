package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.hitanshudhawan.popcorn.adapters.CastAdapter;

import com.hitanshudhawan.popcorn.adapters.MovieBriefsSmallAdapter;
import com.hitanshudhawan.popcorn.adapters.VideoAdapter;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.CastBrief;
import com.hitanshudhawan.popcorn.network.movies.CreditsResponse;
import com.hitanshudhawan.popcorn.network.movies.Genre;
import com.hitanshudhawan.popcorn.network.movies.Movie;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;
import com.hitanshudhawan.popcorn.network.movies.SimilarMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.Video;
import com.hitanshudhawan.popcorn.network.movies.VideosResponse;
import com.hitanshudhawan.popcorn.utils.Constant;
import com.hitanshudhawan.popcorn.utils.Favourite;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private int mMovieId;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private ConstraintLayout mMovieTabLayout;
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
    private LinearLayout mReleaseAndRuntimeTextLayout;
    private TextView mReleaseAndRuntimeTextView;

    private TextView mTrailerTextView;
    private RecyclerView mTrailerRecyclerView;
    private List<Video> mTrailers;
    private VideoAdapter mTrailerAdapter;

    private View mHorizontalLine;

    private TextView mCastTextView;
    private RecyclerView mCastRecyclerView;
    private List<CastBrief> mCasts;
    private CastAdapter mCastAdapter;

    private TextView mSimilarMoviesTextView;
    private RecyclerView mSimilarMoviesRecyclerView;
    private List<MovieBrief> mSimilarMovies;
    private MovieBriefsSmallAdapter mSimilarMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setTitle("");

        Intent receivedIntent = getIntent();
        mMovieId = receivedIntent.getIntExtra(Constant.MOVIE_ID,-1);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        mPosterWidth = (int)(getResources().getDisplayMetrics().widthPixels * 0.25);
        mPosterHeight = (int)(mPosterWidth/0.66);
        mBackdropWidth = getResources().getDisplayMetrics().widthPixels;
        mBackdropHeight = (int)(mBackdropWidth / 1.77);

        mMovieTabLayout = (ConstraintLayout) findViewById(R.id.layout_toolbar_movie);
        mMovieTabLayout.getLayoutParams().height = mBackdropHeight + (int)(mPosterHeight * 0.9);

        mPosterImageView = (ImageView) findViewById(R.id.image_view_poster);
        mPosterImageView.getLayoutParams().width = mPosterWidth;
        mPosterImageView.getLayoutParams().height = mPosterHeight;
        mPosterProgressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar_poster);

        mBackdropImageView = (ImageView) findViewById(R.id.image_view_backdrop);
        mBackdropImageView.getLayoutParams().height = mBackdropHeight;
        mBackdropProgressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar_backdrop);

        mTitleTextView = (TextView) findViewById(R.id.text_view_title_movie_detail);
        mGenreTextView = (TextView) findViewById(R.id.text_view_genre_movie_detail);
        mYearTextView = (TextView) findViewById(R.id.text_view_year_movie_detail);

        mFavImageButton = (ImageButton) findViewById(R.id.image_button_fav_movie_detail);
        mShareImageButton = (ImageButton) findViewById(R.id.image_button_share_movie_detail);

        mOverviewTextView = (TextView) findViewById(R.id.text_view_overview_movie_detail);
        mReleaseAndRuntimeTextLayout = (LinearLayout) findViewById(R.id.layout_release_and_runtime_movie_detail);
        mReleaseAndRuntimeTextView = (TextView) findViewById(R.id.text_view_release_and_runtime_movie_detail);

        mTrailerTextView = (TextView) findViewById(R.id.text_view_trailer_movie_detail);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_trailers_movie_detail);
        (new LinearSnapHelper()).attachToRecyclerView(mTrailerRecyclerView);
        mTrailers = new ArrayList<>();
        mTrailerAdapter = new VideoAdapter(MovieDetailActivity.this, mTrailers);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mHorizontalLine = (View) findViewById(R.id.view_horizontal_line);

        mCastTextView = (TextView) findViewById(R.id.text_view_cast_movie_detail);
        mCastRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_cast_movie_detail);
        mCasts = new ArrayList<>();
        mCastAdapter = new CastAdapter(MovieDetailActivity.this, mCasts);
        mCastRecyclerView.setAdapter(mCastAdapter);
        mCastRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mSimilarMoviesTextView = (TextView) findViewById(R.id.text_view_similar_movie_detail);
        mSimilarMoviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_similar_movie_detail);
        mSimilarMovies = new ArrayList<>();
        mSimilarMoviesAdapter = new MovieBriefsSmallAdapter(MovieDetailActivity.this, mSimilarMovies);
        mSimilarMoviesRecyclerView.setAdapter(mSimilarMoviesAdapter);
        mSimilarMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        loadActivity();
    }

    private void loadActivity() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Movie> call = apiService.getMovieDetails(mMovieId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, final Response<Movie> response) {
                if(!response.isSuccessful()) {
                    call.clone().enqueue(this);
                    return;
                }

                mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if(appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                            mCollapsingToolbarLayout.setTitle(response.body().getTitle());
                            mToolbar.setVisibility(View.VISIBLE);
                        }
                        else {
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
                                mPosterProgressBar.hide();
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mPosterProgressBar.hide();
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
                                mBackdropProgressBar.hide();
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mBackdropProgressBar.hide();
                                return false;
                            }
                        })
                        .into(mBackdropImageView);

                mTitleTextView.setText(response.body().getTitle());

                setGenres(response.body().getGenres());

                setYear(response.body().getReleaseDate());

                mFavImageButton.setVisibility(View.VISIBLE);
                mShareImageButton.setVisibility(View.VISIBLE);
                setImageButtons(response.body().getId(), response.body().getImdbId());

                mOverviewTextView.setText(response.body().getOverview());

                mReleaseAndRuntimeTextLayout.setVisibility(View.VISIBLE);
                setReleaseAndRuntime(response.body().getReleaseDate(), response.body().getRuntime());

                setTrailers();

                mHorizontalLine.setVisibility(View.VISIBLE);

                setCasts();

                setSimilarMovies();

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    private void setGenres(List<Genre> genresList) {
        String genres = "";
        for (int i=0;i<genresList.size();i++) {
            if(i == genresList.size()-1) {
                genres = genres.concat(genresList.get(i).getGenreName());
            }
            else {
                genres = genres.concat(genresList.get(i).getGenreName()+", ");
            }
        }
        mGenreTextView.setText(genres);
    }

    private void setYear(String releaseDateString) {
        if(releaseDateString != null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(releaseDateString);
                mYearTextView.setText(sdf2.format(releaseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setImageButtons(final Integer movieId, final String imdbId) {
        if(Favourite.isMovieFav(MovieDetailActivity.this, movieId)) {
            mFavImageButton.setTag(Constant.TAG_FAV);
            mFavImageButton.setImageResource(R.mipmap.ic_favorite_white_24dp);
        }
        else  {
            mFavImageButton.setTag(Constant.TAG_NOT_FAV);
            mFavImageButton.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        mFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if((int)mFavImageButton.getTag() == Constant.TAG_FAV) {
                    Favourite.removeMovieFromFav(MovieDetailActivity.this, mMovieId);
                    mFavImageButton.setTag(Constant.TAG_NOT_FAV);
                    mFavImageButton.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
                }
                else {
                    Favourite.addMovieToFav(MovieDetailActivity.this, mMovieId);
                    mFavImageButton.setTag(Constant.TAG_FAV);
                    mFavImageButton.setImageResource(R.mipmap.ic_favorite_white_24dp);
                }
            }
        });
        mShareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imdbId != null) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Intent imdbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + imdbId));
                    startActivity(imdbIntent);
                }
            }
        });
    }

    private void setReleaseAndRuntime(String releaseString, Integer runtime) {
        String releaseAndRuntimeString = "";
        if(releaseString != null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy");
            try {
                Date releaseDate = sdf1.parse(releaseString);
                releaseAndRuntimeString += sdf2.format(releaseDate) + "\n";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            releaseAndRuntimeString = "-\n";
        }
        if(runtime != 0) {
            if(runtime < 60) {
                releaseAndRuntimeString += runtime + " min(s)";
            }
            else {
                releaseAndRuntimeString += runtime/60 + " hr " + runtime%60 + " mins";
            }
        }
        else {
            releaseAndRuntimeString += "-";
        }
        mReleaseAndRuntimeTextView.setText(releaseAndRuntimeString);
    }

    private void setTrailers() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<VideosResponse> call = apiService.getMovieVideos(mMovieId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if(!response.isSuccessful()) {
                    call.clone().enqueue(this);
                    return;
                }

                for(Video video : response.body().getVideos()) {
                    if(video.getSite().equals("YouTube") && video.getType().equals("Trailer"))
                        mTrailers.add(video);
                }
                if(!mTrailers.isEmpty())
                    mTrailerTextView.setVisibility(View.VISIBLE);
                mTrailerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {

            }
        });
    }

    private void setCasts() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CreditsResponse> call = apiService.getMovieCredits(mMovieId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        call.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                if(!response.isSuccessful()) {
                    call.clone().enqueue(this);
                    return;
                }

                mCasts.addAll(response.body().getCasts());
                if(!mCasts.isEmpty())
                    mCastTextView.setVisibility(View.VISIBLE);
                mCastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {

            }
        });
    }

    private void setSimilarMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SimilarMoviesResponse> call = apiService.getSimilarMovies(mMovieId, getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        call.enqueue(new Callback<SimilarMoviesResponse>() {
            @Override
            public void onResponse(Call<SimilarMoviesResponse> call, Response<SimilarMoviesResponse> response) {
                if(!response.isSuccessful()) {
                    call.clone().enqueue(this);
                    return;
                }

                mSimilarMovies.addAll(response.body().getResults());
                if(!mSimilarMovies.isEmpty())
                    mSimilarMoviesTextView.setVisibility(View.VISIBLE);
                mSimilarMoviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SimilarMoviesResponse> call, Throwable t) {

            }
        });
    }

}
