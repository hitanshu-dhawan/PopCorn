package com.hitanshudhawan.popcorn.network.videos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitanshu on 2/8/17.
 */

public class VideosResponse {

    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private List<Video> videos;

    public VideosResponse(Integer id, List<Video> videos) {
        this.id = id;
        this.videos = videos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
