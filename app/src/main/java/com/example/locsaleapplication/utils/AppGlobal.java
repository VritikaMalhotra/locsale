package com.example.locsaleapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.locsaleapplication.BuildConfig;
import com.example.locsaleapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppGlobal {

    public final static int permission_camera_code=786;
    public final static int permission_write_data=788;
    public final static int permission_Read_data=789;
    public final static int permission_Recording_audio=790;

    /*public static final  String main_domain="http://api.mastiappind.com/";
    public static final String base_url=main_domain+"app_api/";
    public static final String api_domain =base_url+"index.php?p=";
    public static final String get_user_data= api_domain +"get_user_data";
    public static final String sendPushNotification= api_domain +"sendPushNotification";*/

    public static final SimpleDateFormat df =
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);

    public static final SimpleDateFormat df2 =
            new SimpleDateFormat("dd-MM-yyyy HH:mmZZ", Locale.ENGLISH);

    public static void showLog(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("Locsale", msg);
        }
    }

    public static void loadImage(Context mContext, String imagePath, ImageView imageView) {
        Glide.with(mContext)
                .load(imagePath)
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
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

    public static void loadImageChat(Context mContext, String imagePath, int size, ImageView imageView) {
        Glide.with(mContext)
                .load(imagePath)
                .placeholder(R.drawable.image_placeholder)
                .override(size, size)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageUser(Context mContext, int imagePath, ImageView imageView) {
        Glide.with(mContext)
                .load(imagePath)
                .placeholder(R.drawable.ic_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String convertNormalDateToTimeAgo(Context context, Date date) {
        try {
            Calendar current_cal = Calendar.getInstance();

            Calendar date_cal = Calendar.getInstance();

            //SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ");

            date_cal.setTime(date);


            long difference = (current_cal.getTimeInMillis() - date_cal.getTimeInMillis()) / 1000;

            if (difference < 86400) {
                if (current_cal.get(Calendar.DAY_OF_YEAR) - date_cal.get(Calendar.DAY_OF_YEAR) == 0) {

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    return sdf.format(date_cal);
                } else
                    return "yesterday";
            } else if (difference < 172800) {
                return "yesterday";
            } else
                return (difference / 86400) + " day ago";

        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            return sdf.format(date);
        }
    }


}
