package app.movie.com.movieapp.mvp.movies.impl;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.movie.com.movieapp.R;
import app.movie.com.movieapp.databinding.ViewMovieItemBinding;
import app.movie.com.movieapp.utils.GlideApp;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;

public class MovieFragmentAdapter extends RecyclerView.Adapter<MovieFragmentAdapter.MovieFragmentViewHolder> {

    //region Members
    /** List of movies from API */
    private List<Movie> mMovies;
    /** On item click publisher */
    private PublishSubject<Movie> mOnItemClickSubject = PublishSubject.create();
    /** On favourite click publisher */
    private PublishSubject<Movie> mOnFavouriteClickSubject = PublishSubject.create();

    //endregion

    /** Major constructor */
    public MovieFragmentAdapter() {
        mMovies = new ArrayList<>();
    }


    @Override
    public MovieFragmentViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ViewMovieItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_movie_item, parent, false);
        return new MovieFragmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieFragmentViewHolder holder, final int position) {
        final Movie movie = mMovies.get(position);
        final Context context = holder.binding.getRoot().getContext();
        holder.binding.setMovie(movie);

        tintFavouriteIcon(holder.binding.ivMovieFavourite, movie.isFavourite);

        //Setup image
        GlideApp.with(context)
                .load(movie.posterUrl)
                .fitCenter()
                .into(holder.binding.ivMovieBanner);

        //Setup click-listeners
        holder.binding.ivMovieFavourite.setOnClickListener(view -> {
            movie.isFavourite = !movie.isFavourite;
            tintFavouriteIcon(holder.binding.ivMovieFavourite, movie.isFavourite);
            mOnFavouriteClickSubject.onNext(movie);
        });

        holder.binding.cvMainHolder.setOnClickListener(view2 -> {
            mOnItemClickSubject.onNext(movie);
        });
    }

    private void tintFavouriteIcon(@NonNull final ImageView view ,final boolean highlight) {
        final Context context = view.getContext();
        if (highlight) {
            view.setColorFilter(ContextCompat.getColor(context,
                    R.color.favourite_tint_color_active), android.graphics.PorterDuff.Mode.SRC_IN);

        } else {
            view.setColorFilter(ContextCompat.getColor(context,
                    R.color.favourite_tint_color_inactive), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    /** Set list of movies */
    public void setMovies(@NonNull final List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    /** Clear list of movies */
    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();

    }

    public Flowable<Movie> getOnItemClickFlowable() {
        return mOnItemClickSubject.toFlowable(BackpressureStrategy.LATEST);
    }

    public Flowable<Movie> getOnFavouriteItemClickFlowable() {
        return mOnFavouriteClickSubject.toFlowable(BackpressureStrategy.LATEST);
    }

    /** Recycler view holder */
    public class MovieFragmentViewHolder extends RecyclerView.ViewHolder {
        /** View binding reference */
        public ViewMovieItemBinding binding;

        public MovieFragmentViewHolder(ViewMovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
