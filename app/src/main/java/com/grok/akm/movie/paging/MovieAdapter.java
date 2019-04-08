package com.grok.akm.movie.paging;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import com.grok.akm.movie.Constant;
import com.grok.akm.movie.DetailsActivity;
import com.grok.akm.movie.R;
import com.grok.akm.movie.pojo.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MoviePageListAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movies;


    public MovieAdapter(Context context,List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    private class ViewHolder extends MoviePageListAdapter.ViewHolder implements View.OnClickListener{

        private ViewHolder(View root){
            new MoviePageListAdapter().super(root);
        }

        @Override
        public void onClick(View v) {
            DetailsPath(movie,v);
        }
    }

    @NonNull
    @Override
    public MoviePageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie, viewGroup, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePageListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.movie = movies.get(i);
        viewHolder.itemView.setOnClickListener(viewHolder);

        viewHolder.title.setText(movies.get(i).getTitle());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH);

        Glide.with(context)
                .asBitmap()
                .load(Constant.getPosterPath(movies.get(i).getPosterPath()))
                .apply(options)
                .into(new BitmapImageViewTarget(viewHolder.poster) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        Palette.from(bitmap).generate(palette -> setBackgroundColor(palette, viewHolder));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



    private void setBackgroundColor(Palette palette,MoviePageListAdapter.ViewHolder holder) {
        holder.titleBackground.setBackgroundColor(palette.getVibrantColor(context
                .getResources().getColor(R.color.black_translucent_60)));
    }

    private void DetailsPath(Movie movie,View view) {
        Intent intent = new Intent(context, DetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constant.MOVIE, movie);
        intent.putExtras(extras);

        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity)context,

                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                new Pair<View, String>(view.findViewById(R.id.movie_poster),
                        DetailsActivity.VIEW_NAME_HEADER_IMAGE),
                new Pair<View, String>(view.findViewById(R.id.movie_name),
                        DetailsActivity.VIEW_NAME_HEADER_TITLE));

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(context, intent, activityOptions.toBundle());
    }

}
