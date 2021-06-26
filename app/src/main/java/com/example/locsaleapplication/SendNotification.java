package com.example.locsaleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.locsaleapplication.FCM.FcmNotificationsSender;
import com.example.locsaleapplication.Fragments.ProfileFragment;

public class SendNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        String token = ProfileFragment.token;
        try{
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,"You have a new FOLLOWER!", "A new follower is added!"
                    ,getApplicationContext(),SendNotification.this);
            notificationsSender.SendNotifications();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finish();


    }
}