package com.example.locsaleapplication.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.locsaleapplication.MainActivity;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

@SuppressWarnings("All")
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    NotificationManager mNotificationManager;

    @Override
    public void onNewToken(@NonNull String stToken) {
        super.onNewToken(stToken);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", stToken);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null && firebaseUser.getUid() != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).updateChildren(map);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (ChatActivity.mContext == null) {
            generateNotification(remoteMessage);
        }
    }

    private void generateNotification(RemoteMessage remoteMessage) {
        // playing audio and vibration when user se reques
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "111");
        builder.setSmallIcon(R.mipmap.splash_image);

        Intent resultIntent = new Intent(this, MainActivity.class);
        if (remoteMessage.getData().keySet().contains("action_type")) {
            resultIntent.putExtra("title", remoteMessage.getData().get("title"));
            resultIntent.putExtra("message", remoteMessage.getData().get("message"));
            resultIntent.putExtra("icon", remoteMessage.getData().get("icon"));
            resultIntent.putExtra("senderid", remoteMessage.getData().get("senderid"));
            resultIntent.putExtra("receiverid", remoteMessage.getData().get("receiverid"));
            resultIntent.putExtra("action_type", remoteMessage.getData().get("action_type"));
        } else if (remoteMessage.getData().keySet().contains("postId")) {
            resultIntent.putExtra("postId", remoteMessage.getData().get("postId"));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(remoteMessage.getData().get("title"));
        builder.setContentText(remoteMessage.getData().get("message"));
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "111";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "channel_11",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }


// notificationId is a unique int for each notification that you must define
        mNotificationManager.notify(100, builder.build());
    }
}