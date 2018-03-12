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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.activities.TVShowDetailActivity;
import com.hitanshudhawan.popcorn.network.tvshows.TVShowBrief;
import com.hitanshudhawan.popcorn.utils.Constants;
import com.hitanshudhawan.popcorn.utils.Favourite;

import java.util.List;

/**
 * Created by hitanshu on 13/8/17.
 */

public class TVShowBriefsSmallAdapter extends RecyclerView.Adapter<TVShowBriefsSmallAdapter.TVShowViewHolder> {

    private Context mContext;
    private List<TVShowBrief> mTVShows;

    public TVShowBriefsSmallAdapter(Context context, List<TVShowBrief> tvShows) {
        mContext = context;
        mTVShows = tvShows;
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TVShowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_show_small, parent, false));
    }

    @Override
    public void onBindViewHolder(TVShowViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342 + mTVShows.get(position).getPosterPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.tvShowPosterImageView);

        if (mTVShows.get(position).getName() != null)
            holder.tvShowTitleTextView.setText(mTVShows.get(position).getName());
        else
            holder.tvShowTitleTextView.setText("");

        if (Favourite.isTVShowFav(mContext, mTVShows.get(position).getId())) {
            holder.tvShowFavImageButton.setImageResource(R.mipmap.ic_favorite_black_18dp);
            holder.tvShowFavImageButton.setEnabled(false);
        } else {
            holder.tvShowFavImageButton.setImageResource(R.mipmap.ic_favorite_border_black_18dp);
            holder.tvShowFavImageButton.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return mTVShows.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {

        public CardView tvShowCard;
        public ImageView tvShowPosterImageView;
        public TextView tvShowTitleTextView;
        public ImageButton tvShowFavImageButton;


        public TVShowViewHolder(View itemView) {
            super(itemView);
            tvShowCard = (CardView) itemView.findViewById(R.id.card_view_show_card);
            tvShowPosterImageView = (ImageView) itemView.findViewById(R.id.image_view_show_card);
            tvShowTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_card);
            tvShowFavImageButton = (ImageButton) itemView.findViewById(R.id.image_button_fav_show_card);

            tvShowPosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            tvShowPosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            tvShowCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TVShowDetailActivity.class);
                    intent.putExtra(Constants.TV_SHOW_ID, mTVShows.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });

            tvShowFavImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Favourite.addTVShowToFav(mContext, mTVShows.get(getAdapterPosition()).getId(), mTVShows.get(getAdapterPosition()).getPosterPath(), mTVShows.get(getAdapterPosition()).getName());
                    tvShowFavImageButton.setImageResource(R.mipmap.ic_favorite_black_18dp);
                    tvShowFavImageButton.setEnabled(false);
                }
            });
        }
    }

}
