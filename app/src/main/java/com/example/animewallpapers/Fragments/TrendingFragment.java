package com.example.animewallpapers.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animewallpapers.Adapters.TrendingAdapter;
import com.example.animewallpapers.Models.Wallpaper;
import com.example.animewallpapers.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrendingFragment extends Fragment {

    RecyclerView recyclerView;
    TrendingAdapter adapter;
    List<Wallpaper> wallpaperList;
    DatabaseReference dbWallpaper;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_trending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmerFrameLayout=view.findViewById(R.id.shimmer2);
        shimmerFrameLayout.startShimmer();
        wallpaperList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_trend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter= new TrendingAdapter(getActivity(),wallpaperList);
        recyclerView.setAdapter(adapter);

        dbWallpaper = FirebaseDatabase.getInstance().getReference("images")
                .child("Zzzz");

        dbWallpaper.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot wallsnap:snapshot.getChildren()){
                        Wallpaper w = wallsnap.getValue(Wallpaper.class);
                        wallpaperList.add(w);
                    }

                    Collections.shuffle(wallpaperList);
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