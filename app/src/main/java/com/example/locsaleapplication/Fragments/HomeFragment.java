package com.example.locsaleapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.Adapter.PostAdapter;
import com.example.locsaleapplication.Model.Post;
import com.example.locsaleapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> followingList;
    DatabaseReference ttlRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false) ;
        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);

        ttlRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS);
        Query oldItems = ttlRef.orderByChild("timestamp").endAt(cutoff);
        oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    Post post = itemSnapshot.getValue(Post.class);
                    if(post.getType().equals("1")){
                        FirebaseDatabase.getInstance().getReference().child("Posts").child(itemSnapshot.getKey())
                                .child("type").setValue("0");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postList);
        recyclerViewPosts.setAdapter(postAdapter);
        followingList = new ArrayList<>();

        checkFollowingUsers();

        return view;
    }

    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                //followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for(String id:followingList){
                        if(post.getPublisher().equals(id) && !post.getType().equals("0")){
                            postList.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}