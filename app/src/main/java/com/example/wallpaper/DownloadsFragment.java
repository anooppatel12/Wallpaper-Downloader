package com.example.wallpaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DownloadsFragment extends Fragment {

    private RecyclerView recyclerView;
    private WallpaperAdapter wallpaperAdapter;
    private ArrayList<String> downloadedWallpapers;
    private TextView noDownloadTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_downloads);
        noDownloadTextView = view.findViewById(R.id.text_view_no_downloads);
        recyclerView.setHasFixedSize(true);
        int numberOfColumns = 2; // Set the number of columns in the grid
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        // Retrieve downloaded wallpapers from SharedPreferences
        downloadedWallpapers = getDownloadedWallpapers();

        // Check if there are downloaded wallpapers
        if (downloadedWallpapers.isEmpty()) {
            noDownloadTextView.setVisibility(View.VISIBLE); // Show the message
            recyclerView.setVisibility(View.GONE); // Hide RecyclerView
        } else {
            noDownloadTextView.setVisibility(View.GONE); // Hide the message
            recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView

            // Convert URLs to WallpaperModel objects
            List<WallpaperModel> wallpaperModels = new ArrayList<>();
            for (String path : downloadedWallpapers) {
                wallpaperModels.add(new WallpaperModel(path)); // Assuming a constructor exists
            }

            // Set adapter, passing null for download listener for now
            wallpaperAdapter = new WallpaperAdapter(getContext(), wallpaperModels, null, false, true); // Show set wallpaper button
            recyclerView.setAdapter(wallpaperAdapter);
        }

        return view;
    }

    private ArrayList<String> getDownloadedWallpapers() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("wallpaper_prefs", getActivity().MODE_PRIVATE);
        Set<String> downloadedWallpapersSet = sharedPreferences.getStringSet("downloaded_wallpapers", new HashSet<>());
        return new ArrayList<>(downloadedWallpapersSet);
    }
}
