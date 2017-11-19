package app.movie.com.movieapp.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;

/** Generic implementation of value/property that triggers subscribers on value change. */
public class RxValue<T> {
    /** Observable and an Observer of the value. */
    private final PublishSubject<T> subject = PublishSubject.create();
    /** Last known value. */
    private T lastValue;

    /** hidden constructor. */
    private RxValue() {
        lastValue = null;
    }

    /** Hidden constructor. */
    private RxValue(final T initialValue) {
        lastValue = initialValue;
    }

    /** Static method for simplified instance creation. Less verbose syntax. */
    @NonNull
    public static <T> RxValue<T> of(T initialValue) {
        return new RxValue<>(initialValue);
    }

    /** Create rx value that does is empty from the beginning. */
    @NonNull
    public static <T> RxValue<T> empty() {
        return new RxValue<>();
    }

    /** Get the current value. */
    @Nullable
    public T value() {
        return lastValue;
    }

    /** Set the instance to a new value. */
    @Nullable
    public T set(final T value) {
        lastValue = value;

        if (subject.hasObservers()) {
            subject.onNext(lastValue);
        }

        return value();
    }

    /** Only get value without subscription on later changes. */
    @NonNull
    public Flowable<T> just() {
        if (lastValue == null) return Flowable.empty();
        return Flowable.just(lastValue);
    }
}