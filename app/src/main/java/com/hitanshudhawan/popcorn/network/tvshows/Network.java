package com.hitanshudhawan.popcorn.network.tvshows;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hitanshu on 14/8/17.
 */

public class Network {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;

    public Network(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
