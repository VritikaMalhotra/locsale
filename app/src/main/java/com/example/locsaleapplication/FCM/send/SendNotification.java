package com.example.locsaleapplication.FCM.send;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification {
    final private static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private static String serverKey =
            "key=" + "AAAAeS0nlO0:APA91bEoIghRQCjvfeS5zc4r5n9mACwK-XWKBOUKGggYMsXlj2VYR74-kWQ7sGTmstG5JOZlpeAuKcx7rgKqalrcq8BseUY0y-WaLYtJ4v5f69KeHye-G2tcddlFXYlV67xh6D2NmcxB";
    final private static String contentType = "application/json";
    final static String TAG = "NOTIFICATION TAG";


    public static void sendNotification(Context mContext,
                                        String stTitle,
                                        String stMessage,
                                        String picture,
                                        String senderId,
                                        String receiverId,
                                        String stToken) {
        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", stTitle);
            notificationBody.put("message", stMessage);
            notificationBody.put("body", stMessage);
            notificationBody.put("icon", picture);
            notificationBody.put("senderid", senderId);
            notificationBody.put("receiverid", receiverId);
            notificationBody.put("action_type", "message");

            notification.put("to", stToken);
            notification.put("data", notificationBody);
            notification.put("notification", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.e(TAG, "onResponse: " + response.toString()),
                error -> Log.e(TAG, "onErrorResponse: Didn't work")){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }
}
