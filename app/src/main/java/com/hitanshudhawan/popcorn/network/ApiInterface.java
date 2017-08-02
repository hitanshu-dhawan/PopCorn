package com.hitanshudhawan.popcorn.network;

import com.hitanshudhawan.popcorn.network.movies.CreditsResponse;
import com.hitanshudhawan.popcorn.network.movies.Movie;
import com.hitanshudhawan.popcorn.network.movies.NowShowingMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.PopularMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.TopRatedMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.UpcomingMovieResponse;
import com.hitanshudhawan.popcorn.network.movies.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hitanshu on 27/7/17.
 */

public interface ApiInterface {

    @GET("movie/now_playing")
    Call<NowShowingMovieResponse> getNowShowingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/popular")
    Call<PopularMovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/upcoming")
    Call<UpcomingMovieResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/top_rated")
    Call<TopRatedMovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<CreditsResponse> getMovieCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

}
