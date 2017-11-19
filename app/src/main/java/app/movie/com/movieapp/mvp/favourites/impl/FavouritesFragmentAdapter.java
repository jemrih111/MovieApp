package app.movie.com.movieapp.mvp.favourites.impl;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.movie.com.movieapp.R;
import app.movie.com.movieapp.databinding.ViewFavouriteItemBinding;
import app.movie.com.movieapp.utils.GlideApp;
import app.movie.com.movieapp.web.data.Movie;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;

public class FavouritesFragmentAdapter extends RecyclerView.Adapter<FavouritesFragmentAdapter.FavouritesFragmentViewHolder> {

    //region Members
    /** List of movies from API */
    private List<Movie> mMovies;
    /** On itemclick publisher */
    private PublishSubject<Movie> mOnItemClickPublisher = PublishSubject.create();
    //endregion

    /** Major constructor */
    public FavouritesFragmentAdapter() {
        mMovies = new ArrayList<>();
    }

    @Override
    public FavouritesFragmentViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ViewFavouriteItemBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.view_favourite_item, parent, false);
        return new FavouritesFragmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouritesFragmentViewHolder holder, final int position) {
        final Context context = holder.binding.getRoot().getContext();
        final Movie movie = mMovies.get(position);

        holder.binding.setMovie(movie);

        //Setup image
        GlideApp.with(context)
                .load(movie.posterUrl)
                .fitCenter()
                .into(holder.binding.ivMovieBanner);

        holder.binding.cvCardView.setOnClickListener(view -> mOnItemClickPublisher.onNext(movie));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setMovies(@NonNull final List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public Flowable<Movie> getOnClickFlowable() {
        return mOnItemClickPublisher.toFlowable(BackpressureStrategy.LATEST);
    }

    /** Recycler view holder */
    public class FavouritesFragmentViewHolder extends RecyclerView.ViewHolder {
        /** Reference to viewbinding */
        public ViewFavouriteItemBinding binding;

        /** Mayor constructor */
        public FavouritesFragmentViewHolder(@NonNull final ViewFavouriteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
