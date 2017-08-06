package com.hitanshudhawan.popcorn.network.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitanshu on 27/7/17.
 */

public class PopularMoviesResponse {

    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("results")
    private List<MovieBrief> results;

    public PopularMoviesResponse(Integer page, Integer totalResults, Integer totalPages, List<MovieBrief> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
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

    public List<MovieBrief> getResults() {
        return results;
    }

    public void setResults(List<MovieBrief> results) {
        this.results = results;
    }
}
