package com.hitanshudhawan.popcorn.network.movies;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hitanshu on 31/7/17.
 */

public class Genre {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String genreName;

    public Genre(Integer id, String genreName) {
        this.id = id;
        this.genreName = genreName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
