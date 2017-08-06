package com.hitanshudhawan.popcorn.network;

import com.hitanshudhawan.popcorn.network.movies.CreditsResponse;
import com.hitanshudhawan.popcorn.network.movies.Movie;
import com.hitanshudhawan.popcorn.network.movies.MovieCastsOfPersonResponse;
import com.hitanshudhawan.popcorn.network.movies.NowShowingMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.Person;
import com.hitanshudhawan.popcorn.network.movies.PopularMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.SimilarMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.TopRatedMoviesResponse;
import com.hitanshudhawan.popcorn.network.movies.UpcomingMoviesResponse;
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
    Call<NowShowingMoviesResponse> getNowShowingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/upcoming")
    Call<UpcomingMoviesResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/top_rated")
    Call<TopRatedMoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<CreditsResponse> getMovieCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Call<SimilarMoviesResponse> getSimilarMovies(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("person/{id}")
    Call<Person> getPersonDetails(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Call<MovieCastsOfPersonResponse> getMovieCastsOfPerson(@Path("id") Integer personId, @Query("api_key") String apiKey);
}
