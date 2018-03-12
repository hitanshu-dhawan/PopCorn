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
import com.hitanshudhawan.popcorn.activities.TVShowDetailActivity;
import com.hitanshudhawan.popcorn.network.tvshows.TVCastOfPerson;
import com.hitanshudhawan.popcorn.utils.Constants;

import java.util.List;

/**
 * Created by hitanshu on 13/8/17.
 */

public class TVCastsOfPersonAdapter extends RecyclerView.Adapter<TVCastsOfPersonAdapter.TVShowViewHolder> {

    public Context mContext;
    private List<TVCastOfPerson> mTVCasts;

    public TVCastsOfPersonAdapter(Context context, List<TVCastOfPerson> tvShows) {
        mContext = context;
        mTVCasts = tvShows;
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TVShowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_show_of_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(TVShowViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342 + mTVCasts.get(position).getPosterPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.tvShowPosterImageView);

        if (mTVCasts.get(position).getName() != null)
            holder.tvShowTitleTextView.setText(mTVCasts.get(position).getName());
        else
            holder.tvShowTitleTextView.setText("");

        if (mTVCasts.get(position).getCharacter() != null && !mTVCasts.get(position).getCharacter().trim().isEmpty())
            holder.castCharacterTextView.setText("as " + mTVCasts.get(position).getCharacter());
        else
            holder.castCharacterTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return mTVCasts.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {

        public CardView tvShowCard;
        public ImageView tvShowPosterImageView;
        public TextView tvShowTitleTextView;
        public TextView castCharacterTextView;

        public TVShowViewHolder(View itemView) {
            super(itemView);
            tvShowCard = (CardView) itemView.findViewById(R.id.card_view_show_cast);
            tvShowPosterImageView = (ImageView) itemView.findViewById(R.id.image_view_show_cast);
            tvShowTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_cast);
            castCharacterTextView = (TextView) itemView.findViewById(R.id.text_view_cast_character_show_cast);

            tvShowPosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            tvShowPosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            tvShowCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TVShowDetailActivity.class);
                    intent.putExtra(Constants.TV_SHOW_ID, mTVCasts.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
