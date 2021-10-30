package com.example.locsaleapplication.presentation.chat.commons;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.utils.SharePref;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("All")
public class ApiRequest {

    /*public static void Call_Api(final Context context,
                                final String url,
                                JSONObject jsonObject,
                                final Callback callback) {


        final String[] urlsplit = url.split("/");
        Log.d("ApiRequest", url);

        if (jsonObject != null)
            Log.d("ApiRequest" + urlsplit[urlsplit.length - 1], jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                response -> {

                    final String[] urlsplit1 = url.split("/");
                    Log.d("ApiRequest" + urlsplit1[urlsplit1.length - 1], response.toString());

                    if (callback != null)
                        callback.Responce(response.toString());

                }, error -> {
            final String[] urlsplit12 = url.split("/");
            Log.d("ApiRequest" + urlsplit12[urlsplit12.length - 1], error.toString());

            if (callback != null)
                callback.Responce(error.toString());
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {

                SharePref sharePref = new SharePref(context);

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("fb-id", sharePref.gerStringValue(SharePref.userId));
                headers.put("version", context.getResources().getString(R.string.version));
                headers.put("device", context.getResources().getString(R.string.device));
                headers.put("tokon", sharePref.gerStringValue(SharePref.userToken));
                headers.put("deviceid", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                Log.d("ApiRequest", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }*/


}
