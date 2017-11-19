package app.movie.com.movieapp.mvp.main.impl;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import app.movie.com.movieapp.R;
import app.movie.com.movieapp.mvp.base.Presenter;

public class MainActivityPresenter implements Presenter {

    //region Members

    /** Reference to model */
    private final MainActivityModel mModel;
    /** Reference to view bridge */
    private final MainActivityView mView;
    /** Reference to interactor */
    private final MainActivityInteractor mInteractor;

    //endregion

    /** Major Constructor */
    public MainActivityPresenter(@NonNull final MainActivityModel model,
                                 @NonNull final MainActivityView view,
                                 @NonNull final MainActivityInteractor interactor) {
        mModel = model;
        mView = view;
        mInteractor = interactor;
    }


    public void dispatchClickEvent(@IdRes final int viewId) {
        switch (viewId) {
            case R.id.tab_favourites:
                onFavouritesButtonClicked();
                break;
            case R.id.tab_movies:
                onMoviesButtonClicked();
                break;
        }
    }

    private void onMoviesButtonClicked() {
        mView.switchToMoviesFragment();
    }

    private void onFavouritesButtonClicked() {
        mView.switchToFavouritesFragment();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
