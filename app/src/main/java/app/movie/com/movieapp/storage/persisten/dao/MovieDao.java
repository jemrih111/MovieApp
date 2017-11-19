package app.movie.com.movieapp.storage.persisten.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.Flowable;

@Dao
public interface MovieDao {
    @Query("DELETE FROM movies")
    void nukeTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(@NonNull final Movie movie);

    @Query("SELECT * FROM movies")
    Flowable<List<Movie>> getAllMovies();

    @Delete
    public void deleteMovies(Movie... movies);
}
