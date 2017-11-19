package app.movie.com.movieapp.mvp.details;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import javax.inject.Inject;

import app.movie.com.movieapp.MovieApplication;
import app.movie.com.movieapp.R;
import app.movie.com.movieapp.databinding.ActivityDetailsBinding;
import app.movie.com.movieapp.injection.scopes.PerActivity;
import app.movie.com.movieapp.mvp.details.impl.DetailsActivityInteractor;
import app.movie.com.movieapp.mvp.details.impl.DetailsActivityModel;
import app.movie.com.movieapp.mvp.details.impl.DetailsActivityPresenter;
import app.movie.com.movieapp.mvp.details.impl.DetailsActivityView;
import app.movie.com.movieapp.mvp.topbar.TopbarFragment;
import app.movie.com.movieapp.utils.GlideApp;
import app.movie.com.movieapp.web.clients.MovieAppClient;
import app.movie.com.movieapp.web.data.Movie;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

public class DetailsActivity extends AppCompatActivity implements DetailsActivityView {

    /** Arguments used when passing imdb-id to the activity */
    public static final String ARGS_IMDB_ID = "imdb-id";


    //region Members
    /** Reference to view binding */
    private ActivityDetailsBinding mBinding;
    private TopbarFragment mTopbarFragment;
    //endregion

    //region Injected Members
    @Inject protected DetailsActivityPresenter mPresenter;
    //endregion

    //region Lifecycle methods
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        //inject dependencies
        getComponent().inject(this);
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        setupView();

        final String imdbId = getIntent().getStringExtra(ARGS_IMDB_ID);
        mPresenter.initialize(imdbId);
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

    private void setupView() {
        mTopbarFragment = TopbarFragment.newInstance(TopbarFragment.Types.DETAILS);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(mBinding.flDetailsTopbarPlaceholder.getId(), mTopbarFragment);
        transaction.commit();
    }

    @Override
    public void updateView(@NonNull final Movie movie) {
        mBinding.setMovie(movie);

        //Setup image
        GlideApp.with(this)
                .load(movie.posterUrl)
                .fitCenter()
                .into(mBinding.ivDetailsBanner);


    }

    @Override
    public void animateToStateData() {
        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBinding.pbProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBinding.vOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBinding.vOverlay.setAnimation(fadeOut);
    }

    //region Dagger injection components
    @NonNull
    @VisibleForTesting
    protected DetailsActivityComponent getComponent() {
        return MovieApplication.getComponents(this)
                .plusDetailsActivity(new MvpModule(this));
    }

    /* This module is added in MovieApplicationComponent */
    public interface Injects {
        @NonNull
        DetailsActivityComponent plusDetailsActivity(@NonNull final MvpModule module);
    }

    @Subcomponent(modules = {MvpModule.class})
    @PerActivity
    public interface DetailsActivityComponent {
        /** Inject dependencies into DetailsActivity instance. */
        void inject(@NonNull final DetailsActivity activity);
    }


    @Module
    @PerActivity
    @SuppressWarnings("unused")
    public static class MvpModule {
        /** View instance. */
        private final DetailsActivityView mView;

        protected MvpModule(@NonNull final DetailsActivityView view) {
            mView = view;
        }

        @Provides
        DetailsActivityView providesView() {
            return mView;
        }

        @Provides
        DetailsActivityPresenter providesPresenter(@NonNull final DetailsActivityModel model,
                                                @NonNull final DetailsActivityView view,
                                                @NonNull final DetailsActivityInteractor interactor) {
            return new DetailsActivityPresenter(model, view, interactor);
        }

        @Provides
        DetailsActivityInteractor providesInteractor(@NonNull final MovieAppClient client) {
            return new DetailsActivityInteractor(client);
        }

        @Provides
        DetailsActivityModel providesModel() {
            return new DetailsActivityModel();
        }
    }
    //endregion
}
