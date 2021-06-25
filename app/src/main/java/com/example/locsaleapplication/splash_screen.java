package com.example.locsaleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.AnnotatedElement;

public class splash_screen extends AppCompatActivity {

   private ImageView splash_img;
    private TextView splach_text;
    private TextView splach_text_1;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splach_text = findViewById(R.id.splach_text);
        splash_img = findViewById(R.id.icon_image);
        splach_text_1 = findViewById(R.id.splach_text_2);
        relativeLayout = findViewById(R.id.relativeLayout);

        relativeLayout.animate().alpha(0f).setDuration(1);

        TranslateAnimation animation = new TranslateAnimation(0,0,0,-1000);
        animation.setDuration(1000);
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
            relativeLayout.animate().alpha(1f).setDuration(1000);


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}