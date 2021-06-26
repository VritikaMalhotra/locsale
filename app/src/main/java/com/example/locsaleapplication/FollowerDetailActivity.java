package com.example.locsaleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.locsaleapplication.Model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FollowerDetailActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    String subName;
    private TextView business_name,business_field,mobile_number,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_detail);

        business_name = findViewById(R.id.business_name);
        business_field = findViewById(R.id.business_field);
        mobile_number = findViewById(R.id.mobile_number);
        address = findViewById(R.id.address);

        subName = getIntent().getStringExtra("publisherId");
        userInfo();

    }
    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(subName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                business_name.setText(user.getBusiness_name());
                business_field.setText(user.getBusiness_field());
                mobile_number.setText(user.getContact_number());
                address.setText(user.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}