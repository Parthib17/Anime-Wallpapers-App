package com.example.animewallpapers.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.animewallpapers.Adapters.FavouriteAdapter;
import com.example.animewallpapers.Adapters.TrendingAdapter;
import com.example.animewallpapers.Models.Wallpaper;
import com.example.animewallpapers.R;
import com.example.animewallpapers.room.FavouriteFire;
import com.example.animewallpapers.room.FavouriteModel;
import com.example.animewallpapers.room.FavouriteQuery;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouritesFragment extends Fragment {

    View layout;
    RecyclerView recyclerView;
    FavouriteAdapter adapter;
    List<Wallpaper> wallpaperList = new ArrayList<>();
    ShimmerFrameLayout shimmerFrameLayout;
    public static String dbName ="favDb";
    TextView favText,woops;
    ImageView favicon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout= inflater.inflate(R.layout.fragment_favourites, container, false);
        loadFav();

        return layout;
    }

    private void loadFav() {
        favicon=layout.findViewById(R.id.favIcon);
        favText=layout.findViewById(R.id.favtext);
        woops=layout.findViewById(R.id.woops);

        wallpaperList = new ArrayList<>();
        recyclerView = layout.findViewById(R.id.recycler_fav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter= new FavouriteAdapter(getActivity(),wallpaperList);
        recyclerView.setAdapter(adapter);
        wallpaperList.clear();
        favicon.setVisibility(View.VISIBLE);
        favText.setVisibility(View.VISIBLE);
        woops.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        FavouriteFire db = Room.databaseBuilder(layout.getContext(),FavouriteFire.class,dbName).allowMainThreadQueries().build();
        FavouriteQuery userDao = db.favouriteQuery();

        List<FavouriteModel> list = userDao.allwallpapers();
        for (FavouriteModel data : list){
            wallpaperList.add(new Wallpaper(data.getUrl()));
            favicon.setVisibility(View.INVISIBLE);
            favText.setVisibility(View.INVISIBLE);
            woops.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loadFav();
        }
        catch (Exception e){
            Log.i("check","onResume: "+e.toString());
        }

    }
}