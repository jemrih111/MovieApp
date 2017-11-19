package app.movie.com.movieapp.mvp.main.impl;


public interface MainActivityView {
    /** Change view to movies page */
    void switchToMoviesFragment();
    /** Change view to favourites page */
    void switchToFavouritesFragment();
}
