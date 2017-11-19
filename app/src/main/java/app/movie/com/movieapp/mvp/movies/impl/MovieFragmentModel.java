package app.movie.com.movieapp.mvp.movies.impl;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import app.movie.com.movieapp.storage.persisten.dao.MovieDao;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.Flowable;

public class MovieFragmentModel {

    /** Reference to data abstract object */
    private final MovieDao mDao;

    /** Major constructor */
    public MovieFragmentModel(@NonNull final MovieDao dao) {
        mDao = dao;
    }


    /** Remove specific movie from the database */
    public void removeMovieFromDao(@NonNull final Movie movie) {
        mDao.deleteMovies(movie);
    }

    /** Insert specific movie to the database */
    public void insertMovieToDao(@NonNull final Movie movie) {
        mDao.insertMovie(movie);
    }

    /** Get all movies from database */
    public Flowable<List<Movie>> getMovies() {
        return mDao.getAllMovies();
    }


}
