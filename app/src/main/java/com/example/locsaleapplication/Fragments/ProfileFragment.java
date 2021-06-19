package com.example.locsaleapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.Adapter.PhotoAdapter;
import com.example.locsaleapplication.EditProfileActivity;
import com.example.locsaleapplication.FCM.FcmNotificationsSender;
import com.example.locsaleapplication.FollowersActivity;
import com.example.locsaleapplication.LoginActivity;
import com.example.locsaleapplication.Model.Post;
import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.OptionsActivity;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.SendNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerViewSaves;
    private PhotoAdapter postAdaptersaves;
    private List<Post> mySavedPosts;

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private List<Post> myPhotoList;

    private CircleImageView imageProfile;
    private ImageView options;
    private TextView posts;
    //private TextView followers;
    private TextView following;
    private TextView fullname;
    private TextView bio;
    private TextView username;

    private Button myPictures;
    private  Button savedPictures;
    private Button editProfile;

    private FirebaseUser firebaseUser;
    String profileId;
    public static String token;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId","none");
        if(data.equals("none")){
            profileId = firebaseUser.getUid();

        }else{
            //profileId = firebaseUser.getUid()
            profileId = data;
        }

        imageProfile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        //followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        //myPictures = view.findViewById(R.id.my_pictures);
        savedPictures = view.findViewById(R.id.saved_pictures);
        editProfile = view.findViewById(R.id.edit_profile);

        recyclerView = view.findViewById(R.id.recycler_view_pictures);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        myPhotoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(),myPhotoList);
        recyclerView.setAdapter(photoAdapter);

        recyclerViewSaves = view.findViewById(R.id.recycler_view_saved);
        recyclerViewSaves.setHasFixedSize(true);
        recyclerViewSaves.setLayoutManager(new GridLayoutManager(getContext(),3));
        mySavedPosts = new ArrayList<>();
        postAdaptersaves = new PhotoAdapter(getContext(),mySavedPosts);
        recyclerViewSaves.setAdapter(postAdaptersaves);

        userInfo();
        getFollowersAndFollowingCount();
        getPostCount();
        myPhotos();
        getSavedPosts();

        if(profileId.equals(firebaseUser.getUid())){
            editProfile.setText("Edit Profile");
        }else{
            checkFollowingStatus();
        }
       

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFollowingStatus();
                String btnText = editProfile.getText().toString();
                if(btnText.equals("Edit Profile")){
                    // GOTO edit profile
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                }else{

                    if(btnText.equals("Follow")){

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                                .child(profileId).setValue(true);

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers")
                                .child(firebaseUser.getUid()).setValue(true);

                        addNotification(profileId);

                    }else{

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                                .child(profileId).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers")
                                .child(firebaseUser.getUid()).removeValue();
                    }
                }
            }
        });

        //recyclerView.setVisibility(View.VISIBLE);
        recyclerViewSaves.setVisibility(View.VISIBLE);
        /*myPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewSaves.setVisibility(View.GONE);
            }
        });*/
        savedPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView.setVisibility(View.GONE);
                recyclerViewSaves.setVisibility(View.VISIBLE);
            }
        });

        /*followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id",profileId);
                intent.putExtra("title","followers");
                startActivity(intent);
            }
        });*/

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id",profileId);
                intent.putExtra("title","followings");
                startActivity(intent);
            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });
        return view;
    }

    private void getSavedPosts() {
        final List<String> savedIds = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    savedIds.add(snapshot.getKey());
                }
                FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        mySavedPosts.clear();
                        for(DataSnapshot snapshot1: dataSnapshot1.getChildren()){
                            Post post = snapshot1.getValue(Post.class);
                            for(String id: savedIds){
                                if(post.getPostId().equals(id) && !post.getType().equals("0")){
                                    mySavedPosts.add(post);
                                }
                            }
                        }
                        postAdaptersaves.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myPhotos() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPhotoList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getPublisher().equals(profileId) && !post.getType().equals("0")){
                        myPhotoList.add(post);
                    }
                }
                Collections.reverse(myPhotoList);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(profileId).exists()){
                    editProfile.setText("Following");

                }else{
                    editProfile.setText("Follow");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPostCount() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getPublisher().equals(profileId) && !post.getType().equals("0")){
                        counter++;
                    }

                }
                posts.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowersAndFollowingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);
        /*ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Picasso.get().load(user.getImageurl()).resize(300,300).into(imageProfile);
                username.setText(user.getUsername());
                fullname.setText(user.getName());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String UserId) {
        final String[] username = new String[1];
        FirebaseDatabase.getInstance().getReference().child("Users").child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                token = user.getToken();
                username[0] = user.getUsername();
                Toast.makeText(getContext(), "Notification sent to "+username[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        HashMap<String,Object> map = new HashMap<>();

        map.put("userid", firebaseUser.getUid());
        map.put("test","Started following you");
        map.put("postid","");
        map.put("isPost",false);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(UserId).push().setValue(map);

        Intent myIntent = new Intent(getActivity(), SendNotification.class);
        startActivity(myIntent);
    }
}