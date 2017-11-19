package app.movie.com.movieapp.injection.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import app.movie.com.movieapp.injection.scopes.PerApp;
import app.movie.com.movieapp.storage.persisten.MovieAppDatabase;
import app.movie.com.movieapp.storage.persisten.dao.MovieDao;
import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    /** Get system shared preferences */
    @PerApp
    @NonNull
    @Provides
    SharedPreferences providesSharedPreferences(@NonNull final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    /** Get system database */
    @PerApp
    @NonNull
    @Provides
    MovieAppDatabase providesDatabase(@NonNull final Context context) {
        return Room.databaseBuilder(context, MovieAppDatabase.class, "database-movie-app")
                .fallbackToDestructiveMigration()
                .build();
    }


    /** Get movie dao instance */
    @PerApp
    @NonNull
    @Provides
    MovieDao providesMovieDao(@NonNull final MovieAppDatabase db) {
        return db.movieDao();
    }

}
