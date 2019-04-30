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

public class PersonPopular implements Parcelable {
    @SerializedName("page")
    @Expose
    private long page;
    @SerializedName("results")
    @Expose
    private List<PersonPopularResult> results = new ArrayList<>();
    @SerializedName("total_pages")
    @Expose
    private long totalPages;
    @SerializedName("total_results")
    @Expose
    private long totalResults;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public List<PersonPopularResult> getResults() {
        return results;
    }

    public void setResults(List<PersonPopularResult> results) {
        this.results = results;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.page);
        dest.writeTypedList(this.results);
        dest.writeLong(this.totalPages);
        dest.writeLong(this.totalResults);
    }

    public PersonPopular() {
    }

    protected PersonPopular(Parcel in) {
        this.page = in.readLong();
        this.results = in.createTypedArrayList(PersonPopularResult.CREATOR);
        this.totalPages = in.readLong();
        this.totalResults = in.readLong();
    }

    public static final Creator<PersonPopular> CREATOR = new Creator<PersonPopular>() {
        @Override
        public PersonPopular createFromParcel(Parcel source) {
            return new PersonPopular(source);
        }

        @Override
        public PersonPopular[] newArray(int size) {
            return new PersonPopular[size];
        }
    };
}