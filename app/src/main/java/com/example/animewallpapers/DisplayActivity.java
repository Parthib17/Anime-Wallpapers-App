package com.example.animewallpapers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animewallpapers.room.FavouriteFire;
import com.example.animewallpapers.room.FavouriteModel;
import com.example.animewallpapers.room.FavouriteQuery;

import java.io.File;
import java.io.IOException;

public class DisplayActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView fav, apply, down, share;
    TextView fav_txt,down_txt,apply_txt,share_txt;
    WallpaperManager wallpaperManager;
    String url;
    public static String dbName ="favDb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        wallpaperManager=WallpaperManager.getInstance(getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_200));
            getSupportActionBar().hide();
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        imageView = findViewById(R.id.imageViewDis);
        fav = findViewById(R.id.fav);
        apply = findViewById(R.id.apply);
        apply_txt=findViewById(R.id.apply_text);
        down = findViewById(R.id.download);
        share = findViewById(R.id.share);
        fav_txt=findViewById(R.id.fav_text);
        down_txt=findViewById(R.id.download_text);
        share_txt=findViewById(R.id.share_text);

        registerForContextMenu(apply);
        registerForContextMenu(apply_txt);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        Glide.with(this)
                .load(url)
                .into(imageView);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                //setWall();
            }
        });


        apply_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                //setWall();
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
        down_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        if(checkFav(url,DisplayActivity.this)){
            fav.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else{
            fav.setImageResource(R.drawable.hear);
        }

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFav(url,DisplayActivity.this)){
                    removeFav(url,DisplayActivity.this,fav);
                }
                else{
                    addFav(url,fav);
                }
            }
        });
        fav_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFav(url,DisplayActivity.this)){
                    removeFav(url,DisplayActivity.this,fav);
                }
                else{
                    addFav(url,fav);
                }
            }
        });
    }

    public void addFav(String url,ImageView imageView){
        imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
        new AddFav(url).start();
    }
        class AddFav extends Thread{
            FavouriteFire db = Room.databaseBuilder(DisplayActivity.this,FavouriteFire.class,dbName).allowMainThreadQueries().build();
            FavouriteQuery query = db.favouriteQuery();

            String url;
            AddFav(String url){
                this.url=url;
            }

            @Override
            public void run() {
                super.run();

                try {
                    query.insertData(new FavouriteModel(url));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DisplayActivity.this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DisplayActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }


    private void removeFav(String url,Context context,ImageView imageView){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete..?")
                .setMessage("Are you want to remove this wallpaper from Favourite List")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavouriteFire db = Room.databaseBuilder(context,FavouriteFire.class,dbName).allowMainThreadQueries().build();
                        FavouriteQuery userDao = db.favouriteQuery();

                        try{
                            userDao.deleteByUrl(url);
                            Toast.makeText(context, "Removed..", Toast.LENGTH_SHORT).show();
                            imageView.setImageResource(R.drawable.hear);
                        }
                        catch (Exception e){
                            Toast.makeText(context, "Failed to delete..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private boolean checkFav(String url,Context context){
        FavouriteFire db = Room.databaseBuilder(context,FavouriteFire.class,dbName).allowMainThreadQueries().build();
        FavouriteQuery userDao = db.favouriteQuery();
        if (userDao.is_exist(url)){
            return true;
        }
        else {
            return false;
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        LinearLayout homeLayout = dialog.findViewById(R.id.layoutHomeWall);
        LinearLayout lockLayout = dialog.findViewById(R.id.layoutLockWall);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallHome();
            }
        });

        lockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallLock();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    void download(){
        try {
            String title = URLUtil.guessFileName("wallpaper", null, null);
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(title)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + title + ".jpg");
            dm.enqueue(request);
            Toast.makeText(DisplayActivity.this, "Image downloaded.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(DisplayActivity.this, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }

    void setWallHome(){
        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        try{
            wallpaperManager.setBitmap(bitmap,null,true,wallpaperManager.FLAG_SYSTEM);
            Toast.makeText(DisplayActivity.this, "Applied Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            Toast.makeText(DisplayActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    void setWallLock(){
        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
            try{
                wallpaperManager.setBitmap(bitmap,null,true,wallpaperManager.FLAG_LOCK);
                //wallpaperManager.getInstance(this).setStream(bitmap,null,true,wallpaperManager.FLAG_LOCK);
                Toast.makeText(DisplayActivity.this, "Applied Successfully", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e){
                e.printStackTrace();

            }
        }
    }

}