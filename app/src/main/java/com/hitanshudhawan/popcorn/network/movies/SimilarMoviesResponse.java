package com.hitanshudhawan.popcorn.network.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitanshu on 3/8/17.
 */

public class SimilarMoviesResponse {

    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<MovieBrief> results;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;

    public SimilarMoviesResponse(Integer page, List<MovieBrief> results, Integer totalPages, Integer totalResults) {
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MovieBrief> getResults() {
        return results;
    }

    public void setResults(List<MovieBrief> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
