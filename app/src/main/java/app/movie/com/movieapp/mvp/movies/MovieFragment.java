package app.movie.com.movieapp.mvp.movies;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import app.movie.com.movieapp.MovieApplication;
import app.movie.com.movieapp.R;
import app.movie.com.movieapp.databinding.FragmentMovieBinding;
import app.movie.com.movieapp.injection.scopes.PerFragment;
import app.movie.com.movieapp.mvp.details.DetailsActivity;
import app.movie.com.movieapp.mvp.movies.impl.MovieFragmentAdapter;
import app.movie.com.movieapp.mvp.movies.impl.MovieFragmentInteractor;
import app.movie.com.movieapp.mvp.movies.impl.MovieFragmentModel;
import app.movie.com.movieapp.mvp.movies.impl.MovieFragmentPresenter;
import app.movie.com.movieapp.mvp.movies.impl.MovieFragmentView;
import app.movie.com.movieapp.mvp.topbar.TopbarFragment;
import app.movie.com.movieapp.storage.persisten.dao.MovieDao;
import app.movie.com.movieapp.utils.RxBus;
import app.movie.com.movieapp.web.clients.MovieAppClient;
import app.movie.com.movieapp.web.data.Movie;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;


public class MovieFragment extends Fragment implements MovieFragmentView {

    //region Injected members
    @Inject protected MovieFragmentPresenter mPresenter;
    //endregion

    //region Members

    /** Binding reference */
    private FragmentMovieBinding mBinding;
    /** Movie topbar fragment */
    private TopbarFragment mTopbarFragment;

    //endregion

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance() {
        final MovieFragment fragment = new MovieFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    //region Lifecycle Methods
    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        mPresenter.initialize();
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

    /** Initial setup of fragment */
    private void setupView() {
        mTopbarFragment = TopbarFragment.newInstance(TopbarFragment.Types.MOVIES);

        final FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(mBinding.flMovieTopbarPlaceholder.getId(), mTopbarFragment);
        transaction.commit();

        //Setup RecyclerView
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvMovieList.setLayoutManager(layoutManager);
    }

    @Override
    public void setItems(@NonNull final List<Movie> movies) {
        final MovieFragmentAdapter adapter = (MovieFragmentAdapter) mBinding.rvMovieList.getAdapter();
        adapter.setMovies(movies);
    }

    @Override
    public void setAdapter(@NonNull final MovieFragmentAdapter adapter) {
        mBinding.rvMovieList.setAdapter(adapter);
    }

    @Override
    public void openMovieDetailsView(@NonNull final Movie movie) {
        final Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.ARGS_IMDB_ID, movie.imdbId);
        startActivity(intent);
    }

    //region Dagger injection components
    @NonNull
    @VisibleForTesting
    protected MovieFragmentComponent getComponent(@NonNull final Context context) {
        return MovieApplication.getComponents(context)
                .plusMovieFragment(new MvpModule(this));
    }


    /* This module is added in MovieApplicationComponent */
    public interface Injects {
        @NonNull
        MovieFragmentComponent plusMovieFragment(@NonNull final MvpModule module);
    }

    @Subcomponent(modules = {MvpModule.class})
    @PerFragment
    public interface MovieFragmentComponent {
        /** Inject dependencies into MovieFragment instance. */
        void inject(@NonNull final MovieFragment fragment);
    }

    @Module
    @PerFragment
    @SuppressWarnings({"unused"})
    public static class MvpModule {
        /** View instance */
        private final MovieFragmentView view;

        MvpModule(@NonNull final MovieFragmentView view) {
            this.view = view;
        }

        @Provides
        MovieFragmentPresenter providesPresenter(@NonNull final MovieFragmentModel model,
                                                 @NonNull final MovieFragmentView view,
                                                 @NonNull final MovieFragmentInteractor interactor,
                                                 @NonNull final RxBus rxBus) {
            return new MovieFragmentPresenter(model, view, interactor, rxBus);
        }

        @Provides
        MovieFragmentModel providesModel(@NonNull final MovieDao dao) {
            return new MovieFragmentModel(dao);
        }

        @Provides
        MovieFragmentView providesView() {
            return this.view;
        }

        @Provides
        MovieFragmentInteractor providesInteractor(@NonNull final MovieAppClient client) {
            return new MovieFragmentInteractor(client);
        }
    }

    //endregion
}
