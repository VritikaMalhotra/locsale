package com.example.locsaleapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.locsaleapplication.Fragments.ExploreFragment;
import com.example.locsaleapplication.Fragments.HomeFragment;
import com.example.locsaleapplication.Fragments.NotificationFragment;
import com.example.locsaleapplication.Fragments.ProfileFragment;
import com.example.locsaleapplication.Fragments.SearchFragment;
import com.example.locsaleapplication.Fragments.UserProfileFragment;
import com.example.locsaleapplication.Model.Notification;
import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.presentation.Inbox.InboxFragment;
import com.example.locsaleapplication.utils.AppGlobal;
import com.example.locsaleapplication.utils.SharePref;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

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
                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectorFragment)
                            .addToBackStack(null).commit();
                }
                return true;
            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            if (intent.containsKey("action_type")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment())
                        .addToBackStack(null).commit();

                Bundle bundle = new Bundle();
                bundle.putString("title", intent.getString("title", ""));
                bundle.putString("message", intent.getString("message", ""));
                bundle.putString("icon", intent.getString("icon", ""));
                bundle.putString("senderid", intent.getString("senderid", ""));
                bundle.putString("receiverid", intent.getString("receiverid", ""));
                bundle.putString("action_type", intent.getString("action_type", ""));

                InboxFragment inboxFragment = new InboxFragment();
                inboxFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, inboxFragment)
                        .addToBackStack(null).commit();
            } else if (intent.containsKey("postId")) {

                Bundle bundle = new Bundle();
                bundle.putString("postId", intent.getString("postId", ""));
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment)
                        .addToBackStack(null).commit();

            } else {
                String profileId = intent.getString("publisherId");

                getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ProfileFragment())
                        .addToBackStack(null).commit();
                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment())
                    .addToBackStack(null).commit();
        }

        getCurrentUserData();
        getUnreadNotificationCount();
    }

    public void setBottomNavigationItem(int itemId) {
        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);
    }

    public void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment instanceof HomeFragment) {
            finishAffinity();
        } else if (fragment instanceof SearchFragment) {
            ((SearchFragment)fragment).onBackPressedAll();
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

    int totalNotificationCount = 0;

    public void updateNotificationBedge() {
        if (totalNotificationCount > 0) {
            totalNotificationCount = totalNotificationCount - 1;
            bottomNavigationView.getOrCreateBadge(R.id.nav_heart).setNumber(totalNotificationCount);
        }
    }

    private void getUnreadNotificationCount() {

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNotificationCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    notification.setId(snapshot.getKey());
                    if (!notification.isIs_read()) {
                        totalNotificationCount = totalNotificationCount + 1;
                    }
                }
                BadgeDrawable bedge = bottomNavigationView.getOrCreateBadge(R.id.nav_heart);
                bedge.setVisible(true);
                bedge.setNumber(totalNotificationCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppGlobal.showLog("Error : " + databaseError.getMessage());
            }
        });
    }
}