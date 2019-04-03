package com.grok.akm.movie.paging;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.grok.akm.movie.Constant;
import com.grok.akm.movie.R;
import com.grok.akm.movie.pojo.Movie;

public class MoviePageListAdapter  extends PagedListAdapter<Movie, MoviePageListAdapter.ViewHolder> {

    private Context context;

    public MoviePageListAdapter() {
        super(Movie.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie, viewGroup, false);
        context = viewGroup.getContext();
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.title.setText(getItem(i).getTitle());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH);

        Glide.with(context)
                .asBitmap()
                .load(Constant.getPosterPath(getItem(i).getPosterPath()))
                .apply(options)
                .into(new BitmapImageViewTarget(viewHolder.poster) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        Palette.from(bitmap).generate(palette -> setBackgroundColor(palette, viewHolder));
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        View titleBackground;
        TextView title;

        public Movie movie;

        public ViewHolder(View root) {
            super(root);
            poster = (ImageView) root.findViewById(R.id.movie_poster);
            title = (TextView) root.findViewById(R.id.movie_name);
            titleBackground = (View) root.findViewById(R.id.title_background);
        }

    }

    private void setBackgroundColor(Palette palette, ViewHolder holder) {
        holder.titleBackground.setBackgroundColor(palette.getVibrantColor(context
                .getResources().getColor(R.color.black_translucent_60)));
    }
}
