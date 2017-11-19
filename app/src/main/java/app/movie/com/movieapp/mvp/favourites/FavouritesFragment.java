package app.movie.com.movieapp.mvp.favourites;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import app.movie.com.movieapp.MovieApplication;
import app.movie.com.movieapp.R;
import app.movie.com.movieapp.databinding.FragmentFavouritesBinding;
import app.movie.com.movieapp.injection.scopes.PerFragment;
import app.movie.com.movieapp.mvp.details.DetailsActivity;
import app.movie.com.movieapp.mvp.favourites.impl.FavouritesFragmentAdapter;
import app.movie.com.movieapp.mvp.favourites.impl.FavouritesFragmentInteractor;
import app.movie.com.movieapp.mvp.favourites.impl.FavouritesFragmentModel;
import app.movie.com.movieapp.mvp.favourites.impl.FavouritesFragmentPresenter;
import app.movie.com.movieapp.mvp.favourites.impl.FavouritesFragmentView;
import app.movie.com.movieapp.mvp.topbar.TopbarFragment;
import app.movie.com.movieapp.storage.persisten.dao.MovieDao;
import app.movie.com.movieapp.web.data.Movie;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;


public class FavouritesFragment extends Fragment implements FavouritesFragmentView {

    //region Injected Members

    /** Presenter reference */
    @Inject protected FavouritesFragmentPresenter mPresenter;

    //endregion

    //region Members
    /** View binding reference */
    private FragmentFavouritesBinding mBinding;
    /** Favourite topbar fragment */
    private TopbarFragment mTopbarFragment;

    //endregion

    /** Required empty public constructor */
    public FavouritesFragment() {

    }

    public static FavouritesFragment newInstance() {
        final FavouritesFragment fragment = new FavouritesFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    /** Fragment attached to activity, so we in state to inject all our dependencies. */
    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);

        // inject dependencies
        getComponent(context).inject(this);
    }

    @Override
    public void onPause() {
        mPresenter.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    //endregion

    private void setupView() {
        mTopbarFragment = TopbarFragment.newInstance(TopbarFragment.Types.FAVOURITES);

        final FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(mBinding.flFavouriteTopbarPlaceholder.getId(), mTopbarFragment);
        transaction.commit();

        //Setup recyclerview
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mBinding.rvFavouriteList.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void setAdapter(@NonNull final FavouritesFragmentAdapter adapter) {
        mBinding.rvFavouriteList.setAdapter(adapter);
    }

    @Override
    public void openMovieDetailsView(@NonNull final Movie movie) {
        final Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.ARGS_IMDB_ID, movie.imdbId);
        startActivity(intent);
    }

    @Override
    public void setMovies(@NonNull final List<Movie> movies) {
        final FavouritesFragmentAdapter adapter =
                (FavouritesFragmentAdapter) mBinding.rvFavouriteList.getAdapter();
        adapter.setMovies(movies);
    }

    //region Dagger injection components
    @NonNull
    @VisibleForTesting
    protected FavouritesFragment.FavouritesFragmentComponent getComponent(@NonNull final Context context) {
        return MovieApplication.getComponents(context)
                .plusFavouritesFragment(new FavouritesFragment.MvpModule(this));
    }


    /* This module is added in MovieApplicationComponent */
    public interface Injects {
        @NonNull
        FavouritesFragment.FavouritesFragmentComponent plusFavouritesFragment(@NonNull final FavouritesFragment.MvpModule module);
    }

    @Subcomponent(modules = {FavouritesFragment.MvpModule.class})
    @PerFragment
    public interface FavouritesFragmentComponent {
        /**
         * Inject dependencies into FavouritesFragment instance.
         */
        void inject(@NonNull final FavouritesFragment fragment);
    }

    @Module
    @PerFragment
    @SuppressWarnings({"unused"})
    public static class MvpModule {
        /** View instance */
        private final FavouritesFragmentView view;

        MvpModule(@NonNull final FavouritesFragmentView view) {
            this.view = view;
        }

        @Provides
        FavouritesFragmentPresenter providesPresenter(@NonNull final FavouritesFragmentModel model,
                                                 @NonNull final FavouritesFragmentView view,
                                                 @NonNull final FavouritesFragmentInteractor interactor) {
            return new FavouritesFragmentPresenter(model, view, interactor);
        }

        @Provides
        FavouritesFragmentModel providesModel(@NonNull final MovieDao dao) {
            return new FavouritesFragmentModel(dao);
        }

        @Provides
        FavouritesFragmentView providesView() {
            return this.view;
        }

        @Provides
        FavouritesFragmentInteractor providesInteractor() {
            return new FavouritesFragmentInteractor();
        }
    }

    //endregion

}
