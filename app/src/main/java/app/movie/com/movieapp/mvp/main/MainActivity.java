package app.movie.com.movieapp.mvp.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.OnTabSelectListener;

import javax.inject.Inject;

import app.movie.com.movieapp.MovieApplication;
import app.movie.com.movieapp.R;
import app.movie.com.movieapp.databinding.ActivityMainBinding;
import app.movie.com.movieapp.injection.scopes.PerActivity;
import app.movie.com.movieapp.mvp.favourites.FavouritesFragment;
import app.movie.com.movieapp.mvp.main.impl.MainActivityInteractor;
import app.movie.com.movieapp.mvp.main.impl.MainActivityModel;
import app.movie.com.movieapp.mvp.main.impl.MainActivityPresenter;
import app.movie.com.movieapp.mvp.main.impl.MainActivityView;
import app.movie.com.movieapp.mvp.movies.MovieFragment;
import app.movie.com.movieapp.web.clients.MovieAppClient;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    //region Injected members

    /** Presenter reference */
    @Inject protected MainActivityPresenter mPresenter;

    //endregion

    //region Members

    /** Reference to Favourites page fragment */
    private FavouritesFragment mFavouritesFragment;
    /** Reference to Movie page fragment */
    private MovieFragment mMovieFragment;
    /** Reference to view binding */
    private ActivityMainBinding mBinding;

    /** Reference to bottombar tab click listener */
    private final OnTabSelectListener mTabListener = new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes final int tabId) {
            mPresenter.dispatchClickEvent(tabId);
        }
    };

    //endregion




    //region Lifecycle methods


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        //inject dependencies
        getComponent().inject(this);

        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupViews();
    }

    @Override
    protected void onPause() {
        mPresenter.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    //endregion


    @Override
    public void switchToMoviesFragment() {
        if (mMovieFragment == null) mMovieFragment = MovieFragment.newInstance();
        changeShowingFragment(mMovieFragment);
    }

    @Override
    public void switchToFavouritesFragment() {
        if (mFavouritesFragment == null) mFavouritesFragment = FavouritesFragment.newInstance();
        changeShowingFragment(mFavouritesFragment);
    }

    /** Initialize the view */
    private void setupViews() {
        mBinding.bbBottomBar.setOnTabSelectListener(mTabListener);

        //Setup the first fragment
        switchToMoviesFragment();
    }

    /** Change the current showing fragment in the main content view */
    private void changeShowingFragment(@NonNull final Fragment fragment) {
        fragment.setRetainInstance(true);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(mBinding.flMainContent.getId(), fragment);

        transaction.commit();
    }


    //region Dagger injection components
    @NonNull
    @VisibleForTesting
    protected MainActivityComponent getComponent() {
        return MovieApplication.getComponents(this)
                .plusMainActivity(new MvpModule(this));
    }

    /* This module is added in MovieApplicationComponent */
    public interface Injects {
        @NonNull
        MainActivityComponent plusMainActivity(@NonNull final MvpModule module);
    }

    @Subcomponent(modules = {MvpModule.class})
    @PerActivity
    public interface MainActivityComponent {
        /** Inject dependencies into MainActivity instance. */
        void inject(@NonNull final MainActivity activity);
    }


    @Module
    @PerActivity
    @SuppressWarnings("unused")
    public static class MvpModule {
        /** View instance. */
        private final MainActivityView mView;

        protected MvpModule(@NonNull final MainActivityView view) {
            mView = view;
        }

        @Provides
        MainActivityView providesView() {
            return mView;
        }

        @Provides
        MainActivityPresenter providesPresenter(@NonNull final MainActivityModel model,
                                                @NonNull final MainActivityView view,
                                                @NonNull final MainActivityInteractor interactor) {
            return new MainActivityPresenter(model, view, interactor);
        }

        @Provides
        MainActivityInteractor providesInteractor() {
            return new MainActivityInteractor();
        }

        @Provides
        MainActivityModel providesModel(@NonNull final Context context) {
            return new MainActivityModel(context);
        }

    }
    //endregion
}
