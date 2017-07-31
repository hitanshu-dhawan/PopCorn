package com.hitanshudhawan.popcorn.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.movies.FavouriteMoviesUtil;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;

import java.util.List;

/**
 * Created by hitanshu on 30/7/17.
 */

public class MoviesBigAdapter extends RecyclerView.Adapter<MoviesBigAdapter.MoviesViewHolder> {

    Context mContext;
    List<MovieBrief> mMovies;

    public MoviesBigAdapter(Context context, List<MovieBrief> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_movie_big,parent,false));
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, final int position) {
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w780/" + mMovies.get(position).getBackdropPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);
        holder.movieTitleTextView.setText(mMovies.get(position).getTitle());
        if(mMovies.get(position).getVoteAverage() > 0)
            holder.movieRatingTextView.setText(mMovies.get(position).getVoteAverage() + "\u2605");
        else
            holder.movieRatingTextView.setVisibility(View.GONE);
        holder.movieGenreTextView.setText("Drama,Crime,Thriller"); //todo
        if(FavouriteMoviesUtil.isFavouriteMovie(mContext.getApplicationContext(), mMovies.get(position).getId()))
            holder.movieFavImageButton.setImageResource(R.mipmap.ic_favorite_black_18dp);
        else
            holder.movieFavImageButton.setImageResource(R.mipmap.ic_favorite_border_black_18dp);
        holder.movieFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(holder.movieFavImageButton.getTag().toString().equals(mContext.getResources().getString(R.string.not_favourite_movie))) {
                    holder.movieFavImageButton.setImageResource(R.mipmap.ic_favorite_black_18dp);
                    holder.movieFavImageButton.setTag(mContext.getResources().getString(R.string.favourite_movie));
                    FavouriteMoviesUtil.addMovie(mContext.getApplicationContext(), mMovies.get(position).getId());
                }
                else {
                    holder.movieFavImageButton.setImageResource(R.mipmap.ic_favorite_border_black_18dp);
                    holder.movieFavImageButton.setTag(mContext.getResources().getString(R.string.not_favourite_movie));
                    FavouriteMoviesUtil.removeMovie(mContext.getApplicationContext(), mMovies.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;
        public TextView movieRatingTextView;
        public TextView movieGenreTextView;
        public ImageButton movieFavImageButton;


        public MoviesViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.imageview_movie_card);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.textview_title_movie_card);
            movieRatingTextView = (TextView) itemView.findViewById(R.id.textview_rating_movie_card);
            movieGenreTextView = (TextView) itemView.findViewById(R.id.textview_genre_movie_card);
            movieFavImageButton = (ImageButton) itemView.findViewById(R.id.imagebutton_fav_movie_card);
        }
    }

}
