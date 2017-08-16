package com.hitanshudhawan.popcorn.network.search;

/**
 * Created by hitanshu on 15/8/17.
 */

public class SearchResult {

    private Integer id;
    private String posterPath;
    private String name;
    private String mediaType;
    private String overview;
    private String releaseDate;

    public SearchResult() {

    }

    public SearchResult(Integer id, String posterPath, String name, String mediaType, String overview, String releaseDate) {
        this.id = id;
        this.posterPath = posterPath;
        this.name = name;
        this.mediaType = mediaType;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
