package com.hitanshudhawan.popcorn.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.network.Movie;

import java.util.List;

/**
 * Created by hitanshu on 28/7/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;
    private OnBindViewHolderListener mOnBindViewHolderListener;

    private static final int MOVIE_CARD_LARGE_VIEW = 0;
    private static final int MOVIE_CARD_SMALL_VIEW = 1;

    private boolean largeViewIntervalEnabled = false;
    private int largeViewInterval = 5;

    public interface OnBindViewHolderListener {
        void onBindViewHolder(MoviesViewHolder holder, int position);
    }

    public MoviesAdapter(Context context, List<Movie> movies, OnBindViewHolderListener onBindViewHolderListener) {
        mContext = context;
        mMovies = movies;
        mOnBindViewHolderListener = onBindViewHolderListener;
    }

    public boolean isLargeViewIntervalEnabled() {
        return largeViewIntervalEnabled;
    }

    public void setLargeViewIntervalEnabled(boolean largeViewIntervalEnabled) {
        this.largeViewIntervalEnabled = largeViewIntervalEnabled;
    }

    public int getLargeViewInterval() {
        return largeViewInterval;
    }

    public void setLargeViewInterval(int largeViewInterval) {
        this.largeViewInterval = largeViewInterval;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MOVIE_CARD_LARGE_VIEW)
            return new MoviesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_movie_large, parent, false));
        else
            return new MoviesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_movie_small, parent, false));
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        mOnBindViewHolderListener.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (largeViewIntervalEnabled && position % largeViewInterval == 0)
            return MOVIE_CARD_LARGE_VIEW;
        else return MOVIE_CARD_SMALL_VIEW;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        public ImageView movieImageView;
        public TextView movieTitleTextView;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.movie_imageview);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.movie_title_textview);
        }
    }

}
