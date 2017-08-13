package com.hitanshudhawan.popcorn.network.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitanshu on 2/8/17.
 */

public class MovieCreditsResponse {

    @SerializedName("id")
    private Integer id;
    @SerializedName("cast")
    private List<MovieCastBrief> casts;
    @SerializedName("crew")
    private List<MovieCrewBrief> crews;

    public MovieCreditsResponse(Integer id, List<MovieCastBrief> casts, List<MovieCrewBrief> crews) {
        this.id = id;
        this.casts = casts;
        this.crews = crews;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieCastBrief> getCasts() {
        return casts;
    }

    public void setCasts(List<MovieCastBrief> casts) {
        this.casts = casts;
    }

    public List<MovieCrewBrief> getCrews() {
        return crews;
    }

    public void setCrews(List<MovieCrewBrief> crews) {
        this.crews = crews;
    }

}
