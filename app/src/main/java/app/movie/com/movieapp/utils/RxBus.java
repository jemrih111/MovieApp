package app.movie.com.movieapp.utils;

import android.support.annotation.NonNull;

import app.movie.com.movieapp.utils.Event.Events;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/** Reactive eventbus passed around with dagger */
public class RxBus {
    /** Main publisher */
    private PublishSubject<Event> subject = PublishSubject.create();

    /** Pass any event down to event listeners. */
    public void send(@NonNull final Event event) {
        subject.onNext(event);
    }

    /** Subscribe to specific events from the stream */
    public Observable<Event> getEvents(@Events final int eventId) {
        return subject
                .filter(new Predicate<Event>() {
                    @Override
                    public boolean test(final Event event) throws Exception {
                        return event.id == eventId;
                    }
                });
    }
}
