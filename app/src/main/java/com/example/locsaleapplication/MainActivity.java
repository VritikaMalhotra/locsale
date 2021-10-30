package com.example.locsaleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.locsaleapplication.Fragments.ExploreFragment;
import com.example.locsaleapplication.Fragments.HomeFragment;
import com.example.locsaleapplication.Fragments.NotificationFragment;
import com.example.locsaleapplication.Fragments.ProfileFragment;
import com.example.locsaleapplication.Fragments.SearchFragment;
import com.example.locsaleapplication.Fragments.UserProfileFragment;
import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.utils.SharePref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("All")
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        break;
                    case R.id.nav_explore:
                        selectorFragment = new ExploreFragment();
                        break;
                    case R.id.nav_heart:
                        selectorFragment = new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        selectorFragment = new UserProfileFragment();
                        break;
                }
                if(selectorFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,selectorFragment)
                            .addToBackStack(null).commit();
                }
                return true;
            }
        });

        Bundle intent = getIntent().getExtras();
        if(intent!= null){
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("profileId",profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new ProfileFragment())
                    .addToBackStack(null).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new HomeFragment())
                    .addToBackStack(null).commit();
        }

        getCurrentUserData();
    }

    public void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.adContainer);
        if (fragment instanceof HomeFragment) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    User currentUser;
    private void getCurrentUserData() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SharePref sharePref = new SharePref(MainActivity.this);
                sharePref.saveStringValue(SharePref.userId, currentUser.getId());
                sharePref.saveStringValue(SharePref.userImage, currentUser.getImageurl());
                sharePref.saveStringValue(SharePref.userToken, currentUser.getToken());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}