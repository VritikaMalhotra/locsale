package com.example.locsaleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splash_img;
    private ConstraintLayout constraintLayout;
    private TextView splach_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splash_img = findViewById(R.id.icon_image);
        constraintLayout = findViewById(R.id.constraintLayout);
        splach_text = findViewById(R.id.splach_text);

        String text = "<font color=#FA0505>LOC</font><font color=#1505FA>SALE</font>";
        splach_text.setText(Html.fromHtml(text));

        constraintLayout.animate().alpha(0f).setDuration(1);

        TranslateAnimation animation = new TranslateAnimation(0,0,0,-1000);
        animation.setDuration(2000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        splash_img.setAnimation(animation);
    }

    private class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            splash_img.clearAnimation();
            splash_img.setVisibility(View.INVISIBLE);
            constraintLayout.animate().alpha(1f).setDuration(2000);

            final Handler handler2 = new Handler(Looper.getMainLooper());
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }, 2000);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}