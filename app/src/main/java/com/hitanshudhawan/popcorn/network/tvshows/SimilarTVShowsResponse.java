package com.hitanshudhawan.popcorn.network.tvshows;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitanshu on 13/8/17.
 */

public class SimilarTVShowsResponse {

    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<TVShowBrief> results;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;

    public SimilarTVShowsResponse(Integer page, List<TVShowBrief> results, Integer totalPages, Integer totalResults) {
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

    public List<TVShowBrief> getResults() {
        return results;
    }

    public void setResults(List<TVShowBrief> results) {
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
