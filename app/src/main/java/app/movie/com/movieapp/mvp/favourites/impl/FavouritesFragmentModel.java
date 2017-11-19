package app.movie.com.movieapp.mvp.favourites.impl;


import android.support.annotation.NonNull;

import java.util.List;

import app.movie.com.movieapp.storage.persisten.dao.MovieDao;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.Flowable;

public class FavouritesFragmentModel {

    /** Dao reference */
    private final MovieDao mDao;

    public FavouritesFragmentModel(@NonNull final MovieDao dao) {
        mDao = dao;
    }

    public Flowable<List<Movie>> getMoviesFromDao() {
        return mDao.getAllMovies();
    }
}
