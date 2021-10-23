package com.example.locsaleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.locsaleapplication.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("All")
public class ShopkeeperDetailActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    String profileId;
    private TextView business_name,business_field,mobile_number,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_detail);

        business_name = findViewById(R.id.business_name);
        business_field = findViewById(R.id.business_field);
        mobile_number = findViewById(R.id.mobile_number);
        address = findViewById(R.id.address);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId","none");
        if(data.equals("none")){
            profileId = firebaseUser.getUid();

        }else{
            profileId = data;
        }
        userInfo();

    }
    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
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