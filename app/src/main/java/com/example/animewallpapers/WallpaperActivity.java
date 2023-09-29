package com.example.animewallpapers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.animewallpapers.Adapters.WallpaperAdapter;
import com.example.animewallpapers.Models.Wallpaper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    WallpaperAdapter adapter;
    List<Wallpaper> wallpaperList;
    DatabaseReference dbWallpaper;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }
        Intent intent = getIntent();
        String category=intent.getStringExtra("category");


        shimmerFrameLayout=findViewById(R.id.shimmer1);
        shimmerFrameLayout.startShimmer();
        wallpaperList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewWall);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter= new WallpaperAdapter(this,wallpaperList);
        recyclerView.setAdapter(adapter);


        //action bar name change
        dbWallpaper = FirebaseDatabase.getInstance().getReference("images")
                .child(category);

        dbWallpaper.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot wallsnap:snapshot.getChildren()){
                        Wallpaper w = wallsnap.getValue(Wallpaper.class);
                        wallpaperList.add(w);
                    }
                    Collections.reverse(wallpaperList);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}