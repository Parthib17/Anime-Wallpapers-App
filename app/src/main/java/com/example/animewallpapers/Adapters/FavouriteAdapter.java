package com.example.animewallpapers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animewallpapers.DisplayActivity;
import com.example.animewallpapers.Models.Wallpaper;
import com.example.animewallpapers.R;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavViewHolder> {

    private Context mCtx;
    private List<Wallpaper> wallpaperList;

    public FavouriteAdapter(Context mCtx, List<Wallpaper> wallpaperList) {
        this.mCtx = mCtx;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wall,parent,false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Wallpaper w = wallpaperList.get(position);

        //holder.textView.setText(w.cname);
        Glide.with(mCtx)
                .load(w.imgUrl)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView textView;
        ImageView imageView;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView1);
            textView = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition();
            Wallpaper w = wallpaperList.get(p);
            Intent intent = new Intent(mCtx, DisplayActivity.class);
            intent.putExtra("url",w.imgUrl);
            mCtx.startActivity(intent);
        }
    }


}
