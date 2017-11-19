package app.movie.com.movieapp.injection.components;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import app.movie.com.movieapp.injection.modules.MovieApplicationModule;
import app.movie.com.movieapp.injection.modules.StorageModule;
import app.movie.com.movieapp.injection.modules.WebServiceModule;
import app.movie.com.movieapp.injection.scopes.PerApp;
import app.movie.com.movieapp.mvp.details.DetailsActivity;
import app.movie.com.movieapp.mvp.favourites.FavouritesFragment;
import app.movie.com.movieapp.mvp.main.MainActivity;
import app.movie.com.movieapp.mvp.movies.MovieFragment;
import app.movie.com.movieapp.mvp.topbar.TopbarFragment;
import app.movie.com.movieapp.storage.persisten.dao.MovieDao;
import app.movie.com.movieapp.utils.RxBus;
import app.movie.com.movieapp.web.clients.MovieAppClient;
import dagger.Component;

@PerApp
@Component(modules = {
        MovieApplicationModule.class,
        StorageModule.class,
        WebServiceModule.class
})
/** Major app component interface */
public interface MovieApplicationComponent extends
        MainActivity.Injects,
        MovieFragment.Injects,
        FavouritesFragment.Injects,
        TopbarFragment.Injects,
        DetailsActivity.Injects {

    /** Get local preferences of the application. */
    @PerApp
    SharedPreferences sharedPreferences();

    /** Get movie app client */
    @PerApp
    MovieAppClient movieAppClient();

    /** Get dao instance of typ Movie */
    @PerApp
    MovieDao movieDao();

    /** Get eventbus instance */
    @PerApp
    RxBus rxBus();



    /** Force interface inheritance instead of a inner final class. That gives us more options for customization. */
    @Component.Builder
    @SuppressWarnings("unused")
    interface Builder {

        MovieApplicationComponent build();

        Builder applicationModule(@NonNull final MovieApplicationModule applicationModule);

        Builder storageModule(@NonNull final StorageModule storageModule);

        Builder webServiceModule(@NonNull final WebServiceModule webServiceModule);
    }

}
