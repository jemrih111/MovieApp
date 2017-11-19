package app.movie.com.movieapp.storage.persisten;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import app.movie.com.movieapp.storage.persisten.dao.MovieDao;
import app.movie.com.movieapp.web.data.Movie;

@Database(entities =  {Movie.class}, version = 3)
public abstract class MovieAppDatabase extends RoomDatabase {

    //region Dao's

    public abstract MovieDao movieDao();

    //endregion

}
