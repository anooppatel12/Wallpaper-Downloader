package com.example.wallpaperadmin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewWallpapersActivity extends AppCompatActivity {

    private RecyclerView wallpapersRecyclerView;
    private WallpapersAdapter wallpapersAdapter;
    private List<Wallpaper> wallpaperList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpapers);

        wallpapersRecyclerView = findViewById(R.id.recycler_view_wallpapers);
        wallpapersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Pass the delete listener to the adapter
        wallpapersAdapter = new WallpapersAdapter(wallpaperList, this::deleteWallpaper);
        wallpapersRecyclerView.setAdapter(wallpapersAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("wallpapers");
        loadWallpapers();
    }

    private void loadWallpapers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wallpaperList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Wallpaper wallpaper = snapshot.getValue(Wallpaper.class);
                    wallpaperList.add(wallpaper);
                }
                wallpapersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void deleteWallpaper(String imageUrl) {
        databaseReference.orderByChild("imageUrl").equalTo(imageUrl).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}
