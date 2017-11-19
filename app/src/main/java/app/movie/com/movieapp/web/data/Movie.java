package app.movie.com.movieapp.web.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

import java.util.List;

@Entity(tableName = "movies", primaryKeys = {"title", "imdbId"})
public class Movie {

    //region Members
    @Json(name = "imdbID") @NonNull public String imdbId;
    @Json(name = "Title") @NonNull public String title;
    @Json(name = "Year") @NonNull public String year;
    @Json(name = "Type") @NonNull public String type;
    @Json(name = "Poster") @Nullable public String posterUrl;


    @Json(name = "Rated") @Nullable public String rated;
    @Json(name = "Plot") @Nullable public String plot;
    @Json(name = "Runtime") @Nullable public String runtime;
    @Json(name = "Metascore") public String metascore;
    @Json(name = "imdbRating") public String imdbRating;


    /** Flag used to see if movie is favourite or not */
    public boolean isFavourite;
    //endregion



    /** Convenience method */
    public boolean existInList(@NonNull final List<Movie> movies) {
        for (Movie movie : movies) {
            if (movie.title.equals(title) && movie.imdbId.equals(imdbId)) {
                return true;
            }
        }
        return false;
    }
}
