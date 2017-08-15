package com.hitanshudhawan.popcorn.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.activities.PersonDetailActivity;
import com.hitanshudhawan.popcorn.network.movies.MovieCastBrief;
import com.hitanshudhawan.popcorn.utils.Constant;

import java.util.List;

/**
 * Created by hitanshu on 2/8/17.
 */

public class MovieCastsAdapter extends RecyclerView.Adapter<MovieCastsAdapter.CastViewHolder> {

    private Context mContext;
    private List<MovieCastBrief> mCasts;

    public MovieCastsAdapter(Context mContext, List<MovieCastBrief> mCasts) {
        this.mContext = mContext;
        this.mCasts = mCasts;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constant.IMAGE_LOADING_BASE_URL_342 + mCasts.get(position).getProfilePath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.castImageView);

        if (mCasts.get(position).getName() != null)
            holder.nameTextView.setText(mCasts.get(position).getName());
        else
            holder.nameTextView.setText("");

        if (mCasts.get(position).getCharacter() != null)
            holder.characterTextView.setText(mCasts.get(position).getCharacter());
        else
            holder.characterTextView.setText("");

    }

    @Override
    public int getItemCount() {
        return mCasts.size();
    }


    public class CastViewHolder extends RecyclerView.ViewHolder {

        public ImageView castImageView;
        public TextView nameTextView;
        public TextView characterTextView;

        public CastViewHolder(View itemView) {
            super(itemView);
            castImageView = (ImageView) itemView.findViewById(R.id.image_view_cast);
            nameTextView = (TextView) itemView.findViewById(R.id.text_view_cast_name);
            characterTextView = (TextView) itemView.findViewById(R.id.text_view_cast_as);

            castImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PersonDetailActivity.class);
                    intent.putExtra(Constant.PERSON_ID, mCasts.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
