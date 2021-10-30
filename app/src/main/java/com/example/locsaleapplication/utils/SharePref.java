package com.example.locsaleapplication.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("All")
public class SharePref {

    public static final String userId = "userId";
    public static final String userName = "userName";
    public static final String userImage = "userImage";
    public static final String userToken = "userToken";

    SharedPreferences sharedPreferences;

    public SharePref(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences("locsale", MODE_PRIVATE);
    }

    public void saveStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String gerStringValue(String key) {
        String value = sharedPreferences.getString(key, "");
        return value;
    }
}
