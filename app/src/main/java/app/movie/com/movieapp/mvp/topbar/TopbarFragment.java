package app.movie.com.movieapp.mvp.topbar;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import javax.inject.Inject;

import app.movie.com.movieapp.MovieApplication;
import app.movie.com.movieapp.R;
import app.movie.com.movieapp.databinding.FragmentTopbarBinding;
import app.movie.com.movieapp.injection.scopes.PerFragment;
import app.movie.com.movieapp.mvp.details.DetailsActivity;
import app.movie.com.movieapp.mvp.topbar.impl.TopbarFragmentInteractor;
import app.movie.com.movieapp.mvp.topbar.impl.TopbarFragmentModel;
import app.movie.com.movieapp.mvp.topbar.impl.TopbarFragmentPresenter;
import app.movie.com.movieapp.mvp.topbar.impl.TopbarFragmentView;
import app.movie.com.movieapp.utils.RxBus;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;


public class TopbarFragment extends Fragment implements TopbarFragmentView {

    /** Arguments key for topbar type */
    private static final String ARGS_TYPE = "topbar-type";

    /** Annotation interface for different type of the topbar */
    public @interface Types {
        int MOVIES = 1;
        int FAVOURITES = 2;
        int DETAILS = 3;
    }

    //region Members
    /** Reference to view binding */
    private FragmentTopbarBinding mBinding;

    //endregion


    //region Injected Members
    /** Reference to presenter */
    protected @Inject TopbarFragmentPresenter mPresenter;
    //endregion


    /** Required empty public constructor */
    public TopbarFragment() {

    }

    public static TopbarFragment newInstance(@Types final int type) {
        final TopbarFragment fragment = new TopbarFragment();
        final Bundle args = new Bundle();
        args.putInt(ARGS_TYPE,type);
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
                             @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_topbar, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle bundle = getArguments();
        final int type = bundle.getInt(ARGS_TYPE);
        mPresenter.initialize(type);
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

    /** Fragment attached to activity, so we in state to inject all our dependencies. */
    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);

        // inject dependencies
        getComponent(context).inject(this);
    }

    //endregion


    @Override
    public void initializeMovieTopbar() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.view_movie_topbar, mBinding.flTopBarMainContent, true);
        final EditText editText = view.findViewById(R.id.et_search_field);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                final String input = editable.toString();
                mPresenter.onSearhInput(input);
            }
        });


    }

    @Override
    public void initializeDetailsTopbar() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mBinding.toolbar);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void initializeFavouritesTopbar() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_favourites_topbar, mBinding.flTopBarMainContent, true);
    }

    //region Dagger injection components
    @NonNull
    @VisibleForTesting
    protected TopbarFragmentComponent getComponent(@NonNull final Context context) {
        return MovieApplication.getComponents(context)
                .plusTopbarFragment(new MvpModule(this));
    }


    /* This module is added in MovieApplicationComponent */
    public interface Injects {
        @NonNull
        TopbarFragmentComponent plusTopbarFragment(@NonNull final MvpModule module);
    }

    @Subcomponent(modules = {MvpModule.class})
    @PerFragment
    public interface TopbarFragmentComponent {
        /** Inject dependencies into TopbarFragment instance. */
        void inject(@NonNull final TopbarFragment fragment);
    }

    @Module
    @PerFragment
    @SuppressWarnings({"unused"})
    public static class MvpModule {
        /** View instance */
        private final TopbarFragmentView view;

        MvpModule(@NonNull final TopbarFragmentView view) {
            this.view = view;
        }

        @Provides
        TopbarFragmentPresenter providesPresenter(@NonNull final TopbarFragmentModel model,
                                                  @NonNull final TopbarFragmentView view,
                                                  @NonNull final TopbarFragmentInteractor interactor,
                                                  @NonNull final RxBus rxBus) {
            return new TopbarFragmentPresenter(model, view, interactor, rxBus);
        }

        @Provides
        TopbarFragmentModel providesModel() {
            return new TopbarFragmentModel();
        }

        @Provides
        TopbarFragmentView providesView() {
            return this.view;
        }

        @Provides
        TopbarFragmentInteractor providesInteractor() {
            return new TopbarFragmentInteractor();
        }
    }
    //endregion
}
