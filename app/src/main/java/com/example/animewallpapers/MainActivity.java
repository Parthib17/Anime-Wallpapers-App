package com.example.animewallpapers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.animewallpapers.Fragments.CategoryFragment;
import com.example.animewallpapers.Fragments.FavouritesFragment;
import com.example.animewallpapers.Fragments.TrendingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    BottomNavigationView bottomNavigationView;
    TrendingFragment trendingFragment = new TrendingFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    FavouritesFragment favouritesFragment = new FavouritesFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        FirebaseMessaging.getInstance().subscribeToTopic("hello");

        getSupportFragmentManager().beginTransaction().replace(R.id.container,trendingFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.trending:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,trendingFragment).commit();
                        return true;

                    case R.id.category:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,categoryFragment).commit();
                        return true;

                    case R.id.favourites:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,favouritesFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

}