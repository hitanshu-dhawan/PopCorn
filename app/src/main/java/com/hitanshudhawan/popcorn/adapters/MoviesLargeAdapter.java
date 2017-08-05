package com.hitanshudhawan.popcorn.adapters;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.activities.MovieDetailActivity;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.Genre;
import com.hitanshudhawan.popcorn.network.movies.Movie;
import com.hitanshudhawan.popcorn.network.movies.MovieBrief;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitanshu on 30/7/17.
 */

public class MoviesLargeAdapter extends RecyclerView.Adapter<MoviesLargeAdapter.MoviesViewHolder> {

    private Context mContext;
    private List<MovieBrief> mMovies;

    public MoviesLargeAdapter(Context context, List<MovieBrief> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_movie_large,parent,false));
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, final int position) {

        holder.imageLayout.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.9);
        holder.imageLayout.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.9)/1.77);

        holder.movieCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("movie_id",mMovies.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext.getApplicationContext()).load("https://image.tmdb.org/t/p/w1000/" + mMovies.get(position).getBackdropPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);
        holder.movieTitleTextView.setText(mMovies.get(position).getTitle());
        if(mMovies.get(position).getVoteAverage() > 0)
            holder.movieRatingTextView.setText(mMovies.get(position).getVoteAverage() + "\u2605");
        else
            holder.movieRatingTextView.setVisibility(View.GONE);
        setGenres(holder, mMovies.get(position).getId());
        holder.movieFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                // TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        public CardView movieCard;
        public RelativeLayout imageLayout;
        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;
        public TextView movieRatingTextView;
        public TextView movieGenreTextView;
        public ImageButton movieFavImageButton;


        public MoviesViewHolder(View itemView) {
            super(itemView);
            movieCard = (CardView) itemView.findViewById(R.id.card_view_movie_card);
            imageLayout = (RelativeLayout) itemView.findViewById(R.id.image_layout_movie_card);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.image_view_movie_card);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_movie_card);
            movieRatingTextView = (TextView) itemView.findViewById(R.id.text_view_rating_movie_card);
            movieGenreTextView = (TextView) itemView.findViewById(R.id.text_view_genre_movie_card);
            movieFavImageButton = (ImageButton) itemView.findViewById(R.id.image_button_fav_movie_card);
        }
    }

    private void setGenres(final MoviesViewHolder holder, Integer movieId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Movie> call = apiService.getMovieDetails(movieId,mContext.getResources().getString(R.string.MOVIE_DB_API_KEY));
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.code() != 200) return;
                List<Genre> genresList = response.body().getGenres();
                String genres = "";
                for (int i=0;i<genresList.size();i++) {
                    if(i == genresList.size()-1) {
                        genres = genres.concat(genresList.get(i).getGenreName());
                    }
                    else {
                        genres = genres.concat(genresList.get(i).getGenreName()+", ");
                    }
                }
                holder.movieGenreTextView.setText(genres);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

}
