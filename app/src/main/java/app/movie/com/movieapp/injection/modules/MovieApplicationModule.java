package app.movie.com.movieapp.injection.modules;


import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import app.movie.com.movieapp.MovieApplication;
import app.movie.com.movieapp.injection.scopes.PerApp;
import app.movie.com.movieapp.utils.RxBus;
import dagger.Module;
import dagger.Provides;

@Module
public class MovieApplicationModule {

    /** Reference on application instance */
    private final MovieApplication mApplication;

    public MovieApplicationModule(@NonNull final MovieApplication application) {
        mApplication = application;
    }

    @Provides
    @PerApp
    @NonNull
    MovieApplication providesApplication() {
        return mApplication;
    }

    @Provides
    @PerApp
    @NonNull
    Context providesContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @PerApp
    @NonNull
    Resources providesResources(@NonNull final Context context) {
        return context.getResources();
    }

    @Provides
    @PerApp
    @NonNull
    RxBus providesRxBus() {
        return new RxBus();
    }

}
