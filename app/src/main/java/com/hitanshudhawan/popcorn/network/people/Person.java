package com.hitanshudhawan.popcorn.network.people;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hitanshu on 3/8/17.
 */

public class Person {

    @SerializedName("birthday")
    private String dateOfBirth;
    @SerializedName("deathday")
    private String dateOfDeath;
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    //also_known_as missing
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("biography")
    private String biography;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("place_of_birth")
    private String placeOfBirth;
    @SerializedName("profile_path")
    private String profilePath;
    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("imdb_id")
    private String imdbId;
    //homepage missing


    public Person(String dateOfBirth, String dateOfDeath, Integer id, String name, Integer gender, String biography, Double popularity, String placeOfBirth, String profilePath, Boolean adult, String imdbId) {
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.biography = biography;
        this.popularity = popularity;
        this.placeOfBirth = placeOfBirth;
        this.profilePath = profilePath;
        this.adult = adult;
        this.imdbId = imdbId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

}
