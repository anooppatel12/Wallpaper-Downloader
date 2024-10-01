package com.example.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {

    private Context context;
    private List<WallpaperModel> wallpapers;
    private OnDownloadClickListener onDownloadClickListener;
    private boolean showDownloadButton;
    private boolean showSetWallpaperButton; // New parameter for setting wallpaper button visibility

    public WallpaperAdapter(Context context, List<WallpaperModel> wallpapers,
                            OnDownloadClickListener onDownloadClickListener,
                            boolean showDownloadButton,
                            boolean showSetWallpaperButton) {
        this.context = context;
        this.wallpapers = wallpapers;
        this.onDownloadClickListener = onDownloadClickListener;
        this.showDownloadButton = showDownloadButton;
        this.showSetWallpaperButton = showSetWallpaperButton; // Initialize the new parameter
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallpaper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WallpaperModel wallpaper = wallpapers.get(position);

        // Load the wallpaper image using Glide
        Glide.with(context)
                .load(wallpaper.getImageUrl())
                .into(holder.imageView);

        // Set visibility of the download button
        if (showDownloadButton) {
            holder.downloadButton.setVisibility(View.VISIBLE);
            holder.downloadButton.setOnClickListener(v -> {
                if (onDownloadClickListener != null) {
                    onDownloadClickListener.onDownloadClick(wallpaper.getImageUrl());
                }
            });
        } else {
            holder.downloadButton.setVisibility(View.GONE);
        }

        // Set visibility of the set wallpaper button
        if (showSetWallpaperButton) {
            holder.setWallpaperButton.setVisibility(View.VISIBLE);
            holder.setWallpaperButton.setOnClickListener(v -> {
                setWallpaper(wallpaper.getImageUrl());
            });
        } else {
            holder.setWallpaperButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return wallpapers.size();
    }

    private void setWallpaper(String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                        try {
                            // Set the wallpaper to the home screen and/or lock screen
                            wallpaperManager.setBitmap(((BitmapDrawable) resource).getBitmap());
                            Toast.makeText(context, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error setting wallpaper: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup if necessary
                    }
                });
    }

    public interface OnDownloadClickListener {
        void onDownloadClick(String imageUrl);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button downloadButton;
        Button setWallpaperButton;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_wallpaper);
            downloadButton = itemView.findViewById(R.id.download_button);
            setWallpaperButton = itemView.findViewById(R.id.set_wallpaper_button);
        }
    }
}
