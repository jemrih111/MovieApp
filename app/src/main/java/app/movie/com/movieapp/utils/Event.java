package app.movie.com.movieapp.utils;


import android.support.annotation.Nullable;

public class Event {

    /** Identifier for event */
    @Events public final int id;
    /** additional info associated with event. */
    public final Object tag;

    public Event(@Events final int id) {
        this(id, null);
    }

    public Event(@Events final int id, @Nullable final Object tag) {
        this.id = id;
        this.tag = tag;
    }

    /** Type of events */
    public @interface Events {
        int MOVIE_SEARCH = 1;
    }
}
