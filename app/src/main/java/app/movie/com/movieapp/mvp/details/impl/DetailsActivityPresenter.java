package app.movie.com.movieapp.mvp.details.impl;


import android.support.annotation.NonNull;

import app.movie.com.movieapp.mvp.base.Presenter;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivityPresenter implements Presenter {

    //region Members
    /** Reference to model */
    private final DetailsActivityModel mModel;
    /** Reference to view bridge */
    private final DetailsActivityView mView;
    /** Reference to interactor */
    private final DetailsActivityInteractor mInteractor;
    //endregion



    /** Major constructor */
    public DetailsActivityPresenter(@NonNull final DetailsActivityModel model,
                                    @NonNull final DetailsActivityView view,
                                    @NonNull final DetailsActivityInteractor interactor) {
        mModel = model;
        mView = view;
        mInteractor = interactor;
    }

    public void initialize(@NonNull final String imdbId) {
        mInteractor.getMovieById(imdbId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie -> {
                    mView.updateView(movie);
                    mView.animateToStateData();
                });
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
