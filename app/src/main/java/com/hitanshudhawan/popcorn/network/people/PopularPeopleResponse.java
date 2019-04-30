package com.hitanshudhawan.popcorn.network.people;

/**
 * Create by ANGELO on 19/04/19.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PopularPeopleResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<PeopleBrief> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<PeopleBrief> getResults() {
        return results;
    }

    public void setResults(List<PeopleBrief> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
