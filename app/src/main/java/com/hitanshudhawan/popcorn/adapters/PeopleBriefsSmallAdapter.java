package com.hitanshudhawan.popcorn.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import com.hitanshudhawan.popcorn.activities.PersonDetailActivity;
import com.hitanshudhawan.popcorn.network.person_parce.PersonPopularResult;
import com.hitanshudhawan.popcorn.utils.Constants;
import com.hitanshudhawan.popcorn.utils.Favourite;

/**
 * Created by Angelo on 19/04/19.
 */

public class PeopleBriefsSmallAdapter extends RecyclerView.Adapter<com.hitanshudhawan.popcorn.adapters.PeopleBriefsSmallAdapter.PeopleViewHolder> {

    private static final String TAG = PeopleBriefsSmallAdapter.class.getSimpleName();

    private Context mContext;
    //private List<PeopleBrief> mPeople;
    //OR with Parcelable
    private List<PersonPopularResult> mPeople;

    public PeopleBriefsSmallAdapter(Context context, List<PersonPopularResult> people) {
        mContext = context;
        mPeople = people;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new com.hitanshudhawan.popcorn.adapters.PeopleBriefsSmallAdapter.PeopleViewHolder(LayoutInflater.from(mContext).inflate(com.hitanshudhawan.popcorn.R.layout.item_show_small, parent, false));
    }

    @Override
    public void onBindViewHolder(com.hitanshudhawan.popcorn.adapters.PeopleBriefsSmallAdapter.PeopleViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342 + mPeople.get(position).getProfilePath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.peopleProfilePathView);

        if (mPeople.get(position).getName() != null)
            holder.peopleTitleTextView.setText(mPeople.get(position).getName());
        else
            holder.peopleTitleTextView.setText("");

        if (Favourite.isPeopleFav(mContext, mPeople.get(position).getId())) {
            holder.peopleFavImageButton.setImageResource(com.hitanshudhawan.popcorn.R.mipmap.ic_favorite_black_18dp);
            holder.peopleFavImageButton.setEnabled(false);
        } else {
            holder.peopleFavImageButton.setImageResource(com.hitanshudhawan.popcorn.R.mipmap.ic_favorite_border_black_18dp);
            holder.peopleFavImageButton.setEnabled(true);
        }

        holder.peopleIdTextView.setText("");

    }

    @Override
    public int getItemCount() {
        return mPeople.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {

        public CardView peopleCard;
        public ImageView peopleProfilePathView;
        public TextView peopleTitleTextView;
        public ImageButton peopleFavImageButton;
        public TextView peopleIdTextView;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            peopleCard = (CardView) itemView.findViewById(com.hitanshudhawan.popcorn.R.id.card_view_show_card);
            peopleProfilePathView = (ImageView) itemView.findViewById(com.hitanshudhawan.popcorn.R.id.image_view_show_card);
            peopleTitleTextView = (TextView) itemView.findViewById(com.hitanshudhawan.popcorn.R.id.text_view_title_show_card);
            peopleFavImageButton = (ImageButton) itemView.findViewById(com.hitanshudhawan.popcorn.R.id.image_button_fav_show_card);

            peopleProfilePathView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            peopleProfilePathView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            peopleIdTextView = (TextView) itemView.findViewById(com.hitanshudhawan.popcorn.R.id.text_view_title_show_card);

            /**setOnClickListener**/
            peopleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PersonDetailActivity.class);
                    intent.putExtra(Constants.PEOPLE_ID, mPeople.get(getAdapterPosition()).getId());
                    /** Log for popular activity do not go to person details activity. **/
                    Log.e(TAG, "Constants.PEOPLE_ID"+Constants.PEOPLE_ID);
                    mContext.startActivity(intent);
                }
            });

            peopleFavImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Favourite.addPeopleToFav(mContext, mPeople.get(getAdapterPosition()).getId(), mPeople.get(getAdapterPosition()).getProfilePath(), mPeople.get(getAdapterPosition()).getName());
                    peopleFavImageButton.setImageResource(com.hitanshudhawan.popcorn.R.mipmap.ic_favorite_black_18dp);
                    peopleFavImageButton.setEnabled(false);
                }
            });
        }
    }

}

