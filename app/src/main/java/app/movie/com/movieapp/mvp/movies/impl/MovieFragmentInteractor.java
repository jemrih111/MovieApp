package app.movie.com.movieapp.mvp.movies.impl;


import android.support.annotation.NonNull;

import java.util.List;

import app.movie.com.movieapp.web.clients.MovieAppClient;
import app.movie.com.movieapp.web.data.Movie;
import app.movie.com.movieapp.web.data.SearchResponse;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class MovieFragmentInteractor {

    /** Reference to main web client */
    private final MovieAppClient mClient;

    /** Major constructor */
    public MovieFragmentInteractor(@NonNull final MovieAppClient client) {
        mClient = client;
    }

    /** Get list of movies from the API */
    public Single<List<Movie>> getMovies(@NonNull final String input) {
        return mClient.getMovies(input)
                .map(response -> response.body().movies);
    }
}
