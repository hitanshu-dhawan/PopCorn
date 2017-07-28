package com.hitanshudhawan.popcorn.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitanshu on 28/7/17.
 */

public class UpcomingMovieResponse {

    @SerializedName("results")
    private List<Movie> results;
    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")
    private Integer totalResults;
    //dates missing
    @SerializedName("total_pages")
    private Integer totalPages;

    public UpcomingMovieResponse(List<Movie> results, Integer page, Integer totalResults, Integer totalPages) {
        this.results = results;
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
