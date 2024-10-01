package com.example.wallpaper;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private WallpaperAdapter wallpaperAdapter;
    private List<WallpaperModel> wallpaperList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize the wallpaper list
        wallpaperList = new ArrayList<>();

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("wallpapers");

        // Fetch wallpapers from Firebase
        fetchWallpapersFromFirebase();

        // Set adapter with a no-op listener if downloading is not applicable
        wallpaperAdapter = new WallpaperAdapter(getContext(), wallpaperList, this::downloadWallpaper, true, false);
        recyclerView.setAdapter(wallpaperAdapter);

        return view;
    }

    private void fetchWallpapersFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wallpaperList.clear(); // Clear list before adding new data

                // Loop through all wallpapers in Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    WallpaperModel wallpaper = snapshot.getValue(WallpaperModel.class);
                    if (wallpaper != null) {
                        wallpaperList.add(wallpaper);
                    }
                }

                // Notify the adapter to update the RecyclerView
                wallpaperAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

    private void downloadWallpaper(String imageUrl) {
        if (getActivity() == null) return; // Ensure activity is not null

        // Create a DownloadManager.Request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setTitle("Downloading Wallpaper");
        request.setDescription("Your wallpaper is downloading...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Use a more descriptive file name
        String wallpaperFileName = "wallpaper_" + System.currentTimeMillis() + ".jpg";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, wallpaperFileName);

        // Get the DownloadManager and enqueue the request
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            long downloadId = downloadManager.enqueue(request);
            Log.d("DownloadID", "Download started with ID: " + downloadId);

            // Save the downloaded wallpaper path in SharedPreferences
            saveDownloadedWallpaperPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + wallpaperFileName);
        }
    }

    private void saveDownloadedWallpaperPath(String path) {
        if (getActivity() == null) return; // Ensure activity is not null

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("wallpaper_prefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> downloadedWallpapers = sharedPreferences.getStringSet("downloaded_wallpapers", new HashSet<>());
        downloadedWallpapers.add(path);
        editor.putStringSet("downloaded_wallpapers", downloadedWallpapers);
        editor.apply();
    }
}
