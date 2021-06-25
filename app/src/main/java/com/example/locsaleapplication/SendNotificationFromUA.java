package com.example.locsaleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.locsaleapplication.Adapter.UserAdapter;
import com.example.locsaleapplication.FCM.FcmNotificationsSender;
import com.example.locsaleapplication.Fragments.ProfileFragment;

public class SendNotificationFromUA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification_from_u);

        String token = UserAdapter.token;
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        try{
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,"You have a new FOLLOWER!", "A new follower is added!"
                    ,getApplicationContext(),SendNotificationFromUA.this);
            notificationsSender.SendNotifications();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "in send notification activity UA", Toast.LENGTH_SHORT).show();
        finish();

    }
}