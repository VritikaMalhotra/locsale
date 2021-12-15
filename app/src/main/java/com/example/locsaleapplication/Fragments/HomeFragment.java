package com.example.locsaleapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.Adapter.PostAdapter;
import com.example.locsaleapplication.MainActivity;
import com.example.locsaleapplication.Model.Post;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.Inbox.InboxFragment;
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

@SuppressWarnings("All")
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> followingList;
    DatabaseReference ttlRef;
    private RelativeLayout home_explore;
    private AppCompatButton buttonExplore;
    private ScrollView scroll_view_go_explore;
    private ImageView imageInbox;
    private String postId = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (getArguments() != null && getArguments().containsKey("postId")) {
            postId = getArguments().getString("postId");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setBottomNavigationItem(R.id.nav_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        home_explore = view.findViewById(R.id.home_explore);
        buttonExplore = view.findViewById(R.id.buttonExplore);
        scroll_view_go_explore = view.findViewById(R.id.scroll_view_go_explore);

        imageInbox = view.findViewById(R.id.imageInbox);
        imageInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new InboxFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ttlRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS);
        Query oldItems = ttlRef.orderByChild("timestamp").endAt(cutoff);
        oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Post post = itemSnapshot.getValue(Post.class);
                    if (post.getType().equals("1")) {
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
        postAdapter = new PostAdapter(getContext(), postList, true);
        recyclerViewPosts.setAdapter(postAdapter);
        followingList = new ArrayList<>();

        buttonExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ExploreFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        checkFollowingUsers();
    }

    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                if (dataSnapshot.getChildrenCount() < 1) {
                    home_explore.setVisibility(View.VISIBLE);
                    scroll_view_go_explore.setVisibility(View.VISIBLE);
                    recyclerViewPosts.setVisibility(View.GONE);
                } else {
                    home_explore.setVisibility(View.GONE);
                    scroll_view_go_explore.setVisibility(View.GONE);
                    recyclerViewPosts.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        followingList.add(snapshot.getKey());
                    }
                }
                //followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (followingList.size() > 0) {
                    readPosts();
                }
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
                if (dataSnapshot.getChildrenCount() < 1) {
                    home_explore.setVisibility(View.VISIBLE);
                    scroll_view_go_explore.setVisibility(View.VISIBLE);
                    recyclerViewPosts.setVisibility(View.GONE);
                } else {
                    home_explore.setVisibility(View.GONE);
                    scroll_view_go_explore.setVisibility(View.GONE);
                    recyclerViewPosts.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Post post = snapshot.getValue(Post.class);
                        for (String id : followingList) {
                            if (post.getPublisher().equals(id) && !post.getType().equals("0")) {
                                postList.add(post);
                            }
                        }
                    }
                }

                postAdapter.notifyDataSetChanged();

                if (postId != null && !postId.isEmpty()) {
                    for (Post post : postList) {
                        if (post.getPostId() != null && post.getPostId().equals(postId)) {
                            postId = "";
                            getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postId", post.getPostId())
                                    .apply();
                            ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                                    .add(R.id.frame_container, new PostDetailFragment())
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}