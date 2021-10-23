package com.example.locsaleapplication.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.locsaleapplication.BuildConfig;
import com.example.locsaleapplication.R;

public class AppGlobal {


    public static void showLog(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("Locsale", msg);
        }
    }
    public static void loadImage(Context mContext, String imagePath, int size, ImageView imageView) {
        Glide.with(mContext)
                .load(imagePath)
                .override(size, size)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageUser(Context mContext, String imagePath, int size, ImageView imageView) {
        Glide.with(mContext)
                .load(imagePath)
                .placeholder(R.drawable.ic_profile)
                .override(size, size)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
