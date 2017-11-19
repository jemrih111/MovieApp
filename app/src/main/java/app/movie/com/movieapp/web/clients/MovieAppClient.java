package app.movie.com.movieapp.web.clients;


import android.support.annotation.NonNull;

import java.util.List;

import app.movie.com.movieapp.web.data.Movie;
import app.movie.com.movieapp.web.data.SearchResponse;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieAppClient {

    @GET(".")
    Single<Response<SearchResponse>> getMovies(@Query("s") String search);

    @GET(".")
    /** Get movie by Imdb id */
    Single<Response<Movie>> getMovieById(@Query("i") String id);

}
