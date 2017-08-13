package com.hitanshudhawan.popcorn.network.tvshows;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hitanshu on 13/8/17.
 */

public class TVShowCrewBrief {

    @SerializedName("credit_id")
    private String creditId;
    @SerializedName("department")
    private String department;
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("id")
    private Integer id;
    @SerializedName("job")
    private String job;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profilePath;

    public TVShowCrewBrief(String creditId, String department, Integer gender, Integer id, String job, String name, String profilePath) {
        this.creditId = creditId;
        this.department = department;
        this.gender = gender;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profilePath = profilePath;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
