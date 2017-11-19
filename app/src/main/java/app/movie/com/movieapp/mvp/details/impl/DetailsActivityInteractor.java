package app.movie.com.movieapp.mvp.details.impl;


import android.support.annotation.NonNull;

import app.movie.com.movieapp.web.clients.MovieAppClient;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class DetailsActivityInteractor {

    /** Reference to api client interface */
    private final MovieAppClient mClient;

    /** Major constructor */
    public DetailsActivityInteractor(@NonNull final MovieAppClient client) {
        mClient = client;
    }


    public Single<Movie> getMovieById(@NonNull final String id) {
        return mClient.getMovieById(id)
                .map(Response::body);
    }


}
