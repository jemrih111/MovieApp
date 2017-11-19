package app.movie.com.movieapp.mvp.favourites.impl;


import android.support.annotation.NonNull;

import java.util.List;

import app.movie.com.movieapp.web.data.Movie;

public interface FavouritesFragmentView {
    /** Set adapter for list */
    void setAdapter(@NonNull final FavouritesFragmentAdapter adapter);
    /** Set items to list */
    void setMovies(@NonNull final List<Movie> movies);
    /** Open movie details view */
    void openMovieDetailsView(@NonNull final Movie movie);
}
