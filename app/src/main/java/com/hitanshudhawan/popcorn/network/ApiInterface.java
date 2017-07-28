package com.hitanshudhawan.popcorn.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hitanshu on 27/7/17.
 */

public interface ApiInterface {

    @GET("movie/popular")
    Call<PopularMovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("movie/now_playing")
    Call<NowShowingMovieResponse> getNowShowingMovies(@Query("api_key") String apiKey, @Query("page") Integer page);
}
