package com.hitanshudhawan.popcorn.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.activities.MovieDetailActivity;
import com.hitanshudhawan.popcorn.network.movies.MovieCastOfPerson;
import com.hitanshudhawan.popcorn.utils.Constant;

import java.util.List;

/**
 * Created by hitanshu on 6/8/17.
 */

public class MovieCastsOfPersonAdapter extends RecyclerView.Adapter<MovieCastsOfPersonAdapter.MovieViewHolder> {

    private Context mContext;
    private List<MovieCastOfPerson> mMovieCasts;

    public MovieCastsOfPersonAdapter(Context context, List<MovieCastOfPerson> movies) {
        mContext = context;
        mMovieCasts = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_show_of_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constant.IMAGE_LOADING_BASE_URL_1000 + mMovieCasts.get(position).getPosterPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);

        if (mMovieCasts.get(position).getTitle() != null)
            holder.movieTitleTextView.setText(mMovieCasts.get(position).getTitle());
        else
            holder.movieTitleTextView.setText("");

        if (mMovieCasts.get(position).getCharacter() != null && !mMovieCasts.get(position).getCharacter().trim().isEmpty())
            holder.castCharacterTextView.setText("as " + mMovieCasts.get(position).getCharacter());
        else
            holder.castCharacterTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return mMovieCasts.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public CardView movieCard;
        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;
        public TextView castCharacterTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieCard = (CardView) itemView.findViewById(R.id.card_view_show_cast);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.image_view_show_cast);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_cast);
            castCharacterTextView = (TextView) itemView.findViewById(R.id.text_view_cast_character_show_cast);

            moviePosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            moviePosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(Constant.MOVIE_ID, mMovieCasts.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
