package com.hitanshudhawan.popcorn.network.tvshows;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitanshu on 13/8/17.
 */

public class TVShowCreditsResponse {

    @SerializedName("cast")
    private List<TVShowCastBrief> casts;
    @SerializedName("crew")
    private List<TVShowCrewBrief> crews;
    @SerializedName("id")
    private Integer id;

    public TVShowCreditsResponse(List<TVShowCastBrief> casts, List<TVShowCrewBrief> crews, Integer id) {
        this.casts = casts;
        this.crews = crews;
        this.id = id;
    }

    public List<TVShowCastBrief> getCasts() {
        return casts;
    }

    public void setCasts(List<TVShowCastBrief> casts) {
        this.casts = casts;
    }

    public List<TVShowCrewBrief> getCrews() {
        return crews;
    }

    public void setCrews(List<TVShowCrewBrief> crews) {
        this.crews = crews;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
