package com.example.wallpaper;

public class WallpaperModel {
    private String imageUrl;

    // Empty constructor needed for Firebase
    public WallpaperModel() {
    }

    public WallpaperModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
