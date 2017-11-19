package app.movie.com.movieapp.mvp.topbar.impl;


import android.support.annotation.NonNull;

import app.movie.com.movieapp.mvp.base.Presenter;
import app.movie.com.movieapp.mvp.topbar.TopbarFragment.Types;
import app.movie.com.movieapp.utils.Event;
import app.movie.com.movieapp.utils.RxBus;

public class TopbarFragmentPresenter implements Presenter {

    //region Members
    /** Reference to model */
    private final TopbarFragmentModel mModel;
    /** Reference to interactor */
    private final TopbarFragmentInteractor mInteractor;
    /** Reference to view bridge */
    private final TopbarFragmentView mView;
    /** Reference to evenebbus */
    private final RxBus mRxBus;
    //endregion

    /** Major constructor */
    public TopbarFragmentPresenter(@NonNull final TopbarFragmentModel model,
                                   @NonNull final TopbarFragmentView view,
                                   @NonNull final TopbarFragmentInteractor interactor,
                                   @NonNull final RxBus rxBus) {

        mModel = model;
        mInteractor = interactor;
        mView = view;
        mRxBus = rxBus;
    }

    /** Initialize topbar */
    public void initialize(@Types final int type) {
        switch (type) {
            case Types.MOVIES:
                onInitializeMoviesBar();
                break;
            case Types.FAVOURITES:
                onInitializeFavouritesBar();
                break;
            case Types.DETAILS:
                onInitializeDetailsBar();
                break;
        }
    }

    private void onInitializeMoviesBar() {
        mView.initializeMovieTopbar();
    }

    private void onInitializeFavouritesBar() {
        mView.initializeFavouritesTopbar();
    }

    public void onInitializeDetailsBar() {
        mView.initializeDetailsTopbar();
    }

    public void onSearhInput(@NonNull final String input) {
        mRxBus.send(new Event(Event.Events.MOVIE_SEARCH, input));
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
