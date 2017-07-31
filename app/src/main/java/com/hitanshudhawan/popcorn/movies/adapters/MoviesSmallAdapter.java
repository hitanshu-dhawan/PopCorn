package com.hitanshudhawan.popcorn.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.movies.MovieDetailActivity;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;

import java.util.List;

/**
 * Created by hitanshu on 31/7/17.
 */

public class MoviesSmallAdapter extends RecyclerView.Adapter<MoviesSmallAdapter.MoviesViewHolder> {

    Context mContext;
    List<MovieBrief> mMovies;

    public MoviesSmallAdapter(Context context, List<MovieBrief> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_movie_small,parent,false));
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, final int position) {

        holder.moviePosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
        holder.moviePosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31)/0.66);

        holder.movieCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("movie_id",mMovies.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w1000/" + mMovies.get(position).getPosterPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);
        holder.movieTitleTextView.setText(mMovies.get(position).getTitle());
        holder.movieFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                //TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        public CardView movieCard;
        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;
        public ImageButton movieFavImageButton;


        public MoviesViewHolder(View itemView) {
            super(itemView);
            movieCard = (CardView) itemView.findViewById(R.id.cardview_movie_card);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.imageview_movie_card);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.textview_title_movie_card);
            movieFavImageButton = (ImageButton) itemView.findViewById(R.id.imagebutton_fav_movie_card);
        }
    }

}
