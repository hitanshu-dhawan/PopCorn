package com.hitanshudhawan.popcorn.network.people;

/**
 * Create by ANGELO on 19/04/19.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PeopleBrief {
    @SerializedName("profile_path")
    private String profile_path;
    @SerializedName("adult")
    private boolean adult;

    @SerializedName("peopleId")
    private Integer peopleId;

    //array[object]    OR  OR  OR
    @SerializedName("known_for")
    private List<People_known_for> known_for;

    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private Double popularity;

    public PeopleBrief(String profile_path, boolean adult, Integer peopleId,
                       List<People_known_for> known_for, String name, Double popularity
                 ) {
        this.profile_path = profile_path;
        this.adult = adult;
        this.peopleId = peopleId;

        this.known_for = known_for;
        this.name = name;
        this.popularity = popularity;
    }

    //public PeopleBrief(String profilePath, boolean o, int peopleId, Object known_for, String name, Object popularity) {
    //}

    public String getProfilePath() {
        return profile_path;
    }

    public void setProfilePath(String profile_path) {
        this.profile_path = profile_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public Integer getId() {
        return peopleId;
    }

    public void setId(Integer peopleId) {
        this.peopleId = peopleId;
    }

    public List<People_known_for> getΚnown_for(){
        return known_for;
    }

    public void setΚnown_for(List<People_known_for> known_for) {
        this.known_for = known_for;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getΝame() {
        return name;
    }

    public void setΝame(String name) {
        this.name = name;
    }



}

