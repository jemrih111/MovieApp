package app.movie.com.movieapp.mvp.movies.impl;


import android.support.annotation.NonNull;

import java.util.List;

import app.movie.com.movieapp.web.data.Movie;

public interface MovieFragmentView {
    /** set items for list */
    void setItems(@NonNull final List<Movie> movies);
    /** Set adapter for recyclerview */
    void setAdapter(MovieFragmentAdapter adapter);
    /** Open movie details view */
    void openMovieDetailsView(@NonNull final Movie movie);
}
