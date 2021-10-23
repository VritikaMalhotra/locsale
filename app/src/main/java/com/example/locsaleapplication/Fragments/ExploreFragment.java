package com.example.locsaleapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locsaleapplication.Adapter.PhotoAdapter;
import com.example.locsaleapplication.MainActivity;
import com.example.locsaleapplication.Model.Post;
import com.example.locsaleapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("All")
public class ExploreFragment extends Fragment {
    private RecyclerView recyclerViewExplore;
    private PhotoAdapter photoAdapter;
    private List<Post> myPhotoList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);

        //Back pressed Logic for fragment
        /*view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        *//*Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.commit();*//*
                        getActivity().finish();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });*/


        recyclerViewExplore = view.findViewById(R.id.recycler_view_pictures_search);
        //recyclerViewExplore.setHasFixedSize(true);
        recyclerViewExplore.setLayoutManager(new GridLayoutManager(getContext(),3));
        myPhotoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(),myPhotoList);
        recyclerViewExplore.setAdapter(photoAdapter);

        myPhotos();
        return view;
    }

    private void myPhotos() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPhotoList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(!post.getType().equals("0")){
                        myPhotoList.add(post);
                    }
                }
                //Collections.reverse(myPhotoList);
                //Collections.shuffle(myPhotoList);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}