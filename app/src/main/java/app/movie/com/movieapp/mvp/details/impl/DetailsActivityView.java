package app.movie.com.movieapp.mvp.details.impl;


import android.support.annotation.NonNull;

import app.movie.com.movieapp.web.data.Movie;

public interface DetailsActivityView {
    /** update view with movie object */
    void updateView(@NonNull final Movie movie);

    /** Animate to a state with data */
    void animateToStateData();
}
