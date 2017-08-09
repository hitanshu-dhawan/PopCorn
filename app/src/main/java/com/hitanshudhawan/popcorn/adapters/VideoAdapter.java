package com.hitanshudhawan.popcorn.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.hitanshudhawan.popcorn.network.movies.Video;
import com.hitanshudhawan.popcorn.utils.Constant;

import java.util.List;

/**
 * Created by hitanshu on 2/8/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context mContext;
    private List<Video> mVideos;

    public VideoAdapter(Context mContext, List<Video> videos) {
        this.mContext = mContext;
        this.mVideos = videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {

        holder.videoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.YOUTUBE_WATCH_BASE_URL + mVideos.get(position).getKey()));
                mContext.startActivity(youtubeIntent);
            }
        });
        Glide.with(mContext.getApplicationContext()).load(Constant.YOUTUBE_THUMBNAIL_BASE_URL + mVideos.get(position).getKey() + Constant.YOUTUBE_THUMBNAIL_IMAGE_QUALITY)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.videoImageView);

        holder.videoTextView.setText(mVideos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public CardView videoCard;
        public ImageView videoImageView;
        public TextView videoTextView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoCard = (CardView) itemView.findViewById(R.id.card_view_video);
            videoImageView = (ImageView) itemView.findViewById(R.id.image_view_video);
            videoTextView = (TextView) itemView.findViewById(R.id.text_view_video_name);
        }
    }

}
