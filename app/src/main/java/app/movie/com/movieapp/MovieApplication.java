package app.movie.com.movieapp;


import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import app.movie.com.movieapp.injection.components.DaggerMovieApplicationComponent;
import app.movie.com.movieapp.injection.components.MovieApplicationComponent;
import app.movie.com.movieapp.injection.modules.MovieApplicationModule;


public class MovieApplication extends Application {
    private MovieApplicationComponent mComponents;

    /** Get the instance of the Dagger2 component. */
    public static MovieApplicationComponent getComponents(@NonNull final Context context) {
        return ((MovieApplication) context.getApplicationContext()).mComponents;
    }

    @Override
    public void onCreate() {
        mComponents = initializeDependencies();
        super.onCreate();
    }

    //region Dependencies resolving
    @NonNull
    protected MovieApplicationComponent.Builder newBuilder() {
        return DaggerMovieApplicationComponent.builder();
    }

    /** Create instance of the Dagger component. */
    @NonNull
    private MovieApplicationComponent initializeDependencies() {
        return configureDependencies(newBuilder()).build();
    }

    /** Configure modules before the component instance creation. */
    @VisibleForTesting
    @NonNull
    public MovieApplicationComponent.Builder configureDependencies(
            @NonNull final MovieApplicationComponent.Builder builder) {
        return builder
                .applicationModule(new MovieApplicationModule(this));
    }
}
