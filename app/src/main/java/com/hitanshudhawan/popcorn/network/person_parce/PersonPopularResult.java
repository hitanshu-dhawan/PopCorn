package com.hitanshudhawan.popcorn.network.person_parce;

/**
 * Create by ANGELO on 19/04/19.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.hitanshudhawan.popcorn.network.MoviePerson;

public class PersonPopularResult implements Parcelable, MoviePerson {
    @SerializedName("adult")
    @Expose
    private boolean adult;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("known_for")
    @Expose
    private List<PersonKnown> knownFor = new ArrayList<>();
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PersonKnown> getKnownFor() {
        return knownFor;
    }

    public void setKnownFor(List<PersonKnown> knownFor) {
        this.knownFor = knownFor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
        dest.writeList(this.knownFor);
        dest.writeString(this.name);
        dest.writeDouble(this.popularity);
        dest.writeString(this.profilePath);
    }

    public PersonPopularResult() {
    }

    protected PersonPopularResult(Parcel in) {
        this.adult = in.readByte() != 0;
        this.id = in.readInt();
        this.knownFor = new ArrayList<>();
        in.readList(this.knownFor, PersonKnown.class.getClassLoader());
        this.name = in.readString();
        this.popularity = in.readDouble();
        this.profilePath = in.readString();
    }

    public static final Creator<PersonPopularResult> CREATOR = new Creator<PersonPopularResult>() {
        @Override
        public PersonPopularResult createFromParcel(Parcel source) {
            return new PersonPopularResult(source);
        }

        @Override
        public PersonPopularResult[] newArray(int size) {
            return new PersonPopularResult[size];
        }
    };

    public PersonPopularResult( Boolean adult, int id,
                               List<PersonKnown> knownFor, String name,
                                Double popularite, String profilePath
    ) {
        this.adult = adult;
        this.id = id;

        this.knownFor = knownFor;
        this.name = name;
        this.popularity = popularity;

        this.profilePath = profilePath;
    }

    public PersonPopularResult(  int id, String name, String profilePath) {

        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }
}

