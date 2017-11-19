package app.movie.com.movieapp.mvp.favourites.impl;


import android.support.annotation.NonNull;

import org.reactivestreams.Subscription;

import java.util.List;

import app.movie.com.movieapp.mvp.base.Presenter;
import app.movie.com.movieapp.utils.RxValue;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavouritesFragmentPresenter implements Presenter {

    //region Members
    /** Reference to model */
    private final FavouritesFragmentModel mModel;
    /** Reference to view bridge */
    private final FavouritesFragmentView mView;
    /** Reference to interactor */
    private final FavouritesFragmentInteractor mInteractor;
    /** Reactive value listener for adaper */
    private RxValue<FavouritesFragmentAdapter> mAdapter = RxValue.empty();
    //endregion

    //region Disposables
    private Disposable mMovieFavouriteDisposable;
    private Disposable mOnFavouriteClickDisposable;
    //endregion


    /** Major constructor */
    public FavouritesFragmentPresenter(@NonNull final FavouritesFragmentModel model,
                                       @NonNull final FavouritesFragmentView view,
                                       @NonNull final FavouritesFragmentInteractor interactor) {
        mModel = model;
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onResume() {
        mMovieFavouriteDisposable = mModel.getMoviesFromDao()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (mAdapter.value() == null) {
                        mAdapter.set(new FavouritesFragmentAdapter());
                    }
                    mView.setAdapter(mAdapter.value());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::setMovies);

        mOnFavouriteClickDisposable = mAdapter.just()
                .flatMap(FavouritesFragmentAdapter::getOnClickFlowable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::openMovieDetailsView);
    }

    @Override
    public void onPause() {
        if (mMovieFavouriteDisposable != null) mMovieFavouriteDisposable.dispose();
        if (mOnFavouriteClickDisposable != null) mOnFavouriteClickDisposable.dispose();
    }
}
