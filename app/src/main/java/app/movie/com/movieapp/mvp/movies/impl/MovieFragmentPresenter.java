package app.movie.com.movieapp.mvp.movies.impl;


import android.support.annotation.NonNull;
import android.util.Log;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import app.movie.com.movieapp.mvp.base.Presenter;
import app.movie.com.movieapp.utils.Event;
import app.movie.com.movieapp.utils.RxBus;
import app.movie.com.movieapp.utils.RxValue;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MovieFragmentPresenter implements Presenter {

    //regione Members
    /** Reference to model */
    private final MovieFragmentModel mModel;
    /** Reference to view bridge */
    private final MovieFragmentView mView;
    /** Reference to interactor */
    private final MovieFragmentInteractor mInteractor;
    /** Eventbus reference */
    private final RxBus mRxBus;
    /** Reactive value listener for adapter */
    private RxValue<MovieFragmentAdapter> mAdapter = RxValue.empty();
    /** List of latest favourite movies */
    private List<Movie> mFavouriteMovies;
    //endregion

    //region Disposables
    /** Disposable listening for events on search input */
    private Disposable mSearchDisposable;
    /** Disposable listening on favourite clicked in list */
    private Disposable mOnFavouriteClickedDisposable;
    /** Disposable listening on item click in list */
    private Disposable mOnItemClickDisposable;
    /** Disposable listening on movies dao */
    private Disposable mOnMoviesChangeDisposable;
    //endregion


    /** Major constructor */
    public MovieFragmentPresenter(@NonNull final MovieFragmentModel model,
                                  @NonNull final MovieFragmentView view,
                                  @NonNull final MovieFragmentInteractor interactor,
                                  @NonNull final RxBus rxBus) {
        mModel = model;
        mView = view;
        mInteractor = interactor;
        mRxBus = rxBus;
    }


    public void initialize() {

    }


    @Override
    public void onResume() {
        //Listen for eventbus events from topbar
        mSearchDisposable = mRxBus.getEvents(Event.Events.MOVIE_SEARCH)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (mAdapter.value() ==  null) {
                        mAdapter.set(new MovieFragmentAdapter());
                    }
                    mView.setAdapter(mAdapter.value());
                })
                .doOnNext(event -> mAdapter.value().clear())
                .observeOn(Schedulers.io())
                .filter(event -> {
                    final String input  = (String) event.tag;
                    return input != null && input.length() > 2;
                })
                .flatMapSingle(event -> {
                    final String input = (String )event.tag;
                    return mInteractor.getMovies(input)
                            .onErrorReturnItem(new ArrayList<>());
                })
                .map(movies -> {
                    //Check search with favourites to see if already favourite
                    for (Movie movie : movies) {
                        if (movie.existInList(mFavouriteMovies)) {
                            movie.isFavourite = true;
                        }
                    }
                    return movies;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::setItems);

        mOnItemClickDisposable = mAdapter.just()
                .subscribeOn(Schedulers.io())
                .flatMap(MovieFragmentAdapter::getOnItemClickFlowable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::openMovieDetailsView);

        mOnFavouriteClickedDisposable = mAdapter.just()
                .subscribeOn(Schedulers.io())
                .flatMap(MovieFragmentAdapter::getOnFavouriteItemClickFlowable)
                .observeOn(Schedulers.io())
                .subscribe(movie -> {
                    if (movie.isFavourite) {
                        mModel.insertMovieToDao(movie);
                    } else {
                        mModel.removeMovieFromDao(movie);
                    }
                });

        mOnMoviesChangeDisposable = mModel.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mFavouriteMovies = movies);
    }

    @Override
    public void onPause() {
        if (mSearchDisposable != null) mSearchDisposable.dispose();
        if (mOnItemClickDisposable != null) mOnItemClickDisposable.dispose();
        if (mOnFavouriteClickedDisposable != null) mOnFavouriteClickedDisposable.dispose();
        if (mOnMoviesChangeDisposable != null) mOnMoviesChangeDisposable.dispose();
    }
}
