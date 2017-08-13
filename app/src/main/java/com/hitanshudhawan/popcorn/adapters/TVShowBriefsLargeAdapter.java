package com.hitanshudhawan.popcorn.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.network.tvshows.TVShowBrief;
import com.hitanshudhawan.popcorn.utils.Constant;
import com.hitanshudhawan.popcorn.utils.TVShowGenres;

import java.util.List;

/**
 * Created by hitanshu on 13/8/17.
 */

public class TVShowBriefsLargeAdapter extends RecyclerView.Adapter<TVShowBriefsLargeAdapter.TVShowViewHolder> {

    private Context mContext;
    private List<TVShowBrief> mTVShows;

    public TVShowBriefsLargeAdapter(Context context, List<TVShowBrief> tvShows) {
        mContext = context;
        mTVShows = tvShows;
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TVShowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_show_large, parent, false));
    }

    @Override
    public void onBindViewHolder(TVShowViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constant.IMAGE_LOADING_BASE_URL_1000 + mTVShows.get(position).getBackdropPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.tvShowPosterImageView);

        if (mTVShows.get(position).getName() != null)
            holder.tvShowTitleTextView.setText(mTVShows.get(position).getName());
        else
            holder.tvShowTitleTextView.setText("");

        if (mTVShows.get(position).getVoteAverage() != null && mTVShows.get(position).getVoteAverage() > 0) {
            holder.tvShowRatingTextView.setVisibility(View.VISIBLE);
            holder.tvShowRatingTextView.setText(mTVShows.get(position).getVoteAverage() + Constant.RATING_SYMBOL);
        } else {
            holder.tvShowRatingTextView.setVisibility(View.GONE);
        }

        setGenres(holder, mTVShows.get(position));

//        if (Favourite.isMovieFav(mContext, mTVShows.get(position).getId())) {
//            holder.tvShowFavImageButton.setImageResource(R.mipmap.ic_favorite_black_18dp);
//            holder.tvShowFavImageButton.setEnabled(false);
//        } else {
//            holder.tvShowFavImageButton.setImageResource(R.mipmap.ic_favorite_border_black_18dp);
//            holder.tvShowFavImageButton.setEnabled(true);
//        }
    }

    @Override
    public int getItemCount() {
        return mTVShows.size();
    }

    private void setGenres(TVShowViewHolder holder, TVShowBrief tvShow) {
        String genreString = "";
        for (int i = 0; i < tvShow.getGenreIds().size(); i++) {
            if (tvShow.getGenreIds().get(i) == null) continue;
            if (TVShowGenres.getGenreName(tvShow.getGenreIds().get(i)) == null) continue;
            genreString += TVShowGenres.getGenreName(tvShow.getGenreIds().get(i)) + ", ";
        }
        holder.tvShowGenreTextView.setText(genreString.substring(0,genreString.length()-2));
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {

        public CardView tvShowCard;
        public RelativeLayout imageLayout;
        public ImageView tvShowPosterImageView;
        public TextView tvShowTitleTextView;
        public TextView tvShowRatingTextView;
        public TextView tvShowGenreTextView;
        public ImageButton tvShowFavImageButton;


        public TVShowViewHolder(View itemView) {
            super(itemView);
            tvShowCard = (CardView) itemView.findViewById(R.id.card_view_show_card);
            imageLayout = (RelativeLayout) itemView.findViewById(R.id.image_layout_show_card);
            tvShowPosterImageView = (ImageView) itemView.findViewById(R.id.image_view_show_card);
            tvShowTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_card);
            tvShowRatingTextView = (TextView) itemView.findViewById(R.id.text_view_rating_show_card);
            tvShowGenreTextView = (TextView) itemView.findViewById(R.id.text_view_genre_show_card);
            tvShowFavImageButton = (ImageButton) itemView.findViewById(R.id.image_button_fav_show_card);

            imageLayout.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.9);
            imageLayout.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.9) / 1.77);

            tvShowCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
//                    intent.putExtra(Constant.MOVIE_ID, mMovies.get(getAdapterPosition()).getId());
//                    mContext.startActivity(intent);
                }
            });

            tvShowFavImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    Favourite.addMovieToFav(mContext, mMovies.get(getAdapterPosition()).getId());
//                    movieFavImageButton.setImageResource(R.mipmap.ic_favorite_black_18dp);
//                    movieFavImageButton.setEnabled(false);
                }
            });
        }
    }
}
