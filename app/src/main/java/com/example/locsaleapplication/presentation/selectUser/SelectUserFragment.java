package com.example.locsaleapplication.presentation.selectUser;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.chat.ChatFunctions;
import com.example.locsaleapplication.utils.AppGlobal;
import com.example.locsaleapplication.utils.OnItemClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("All")
public class SelectUserFragment extends Fragment {


    View view;
    Context context;

    RecyclerView inbox_list;

    DatabaseReference root_ref;

    ProgressBar pbar;

    boolean isview_created = false;
    private FirebaseUser firebaseUser;
    //private String profileId;

    public SelectUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select_user, container, false);
        context = getContext();
        return view;
    }

    public void loadBanner(View view) {
        FrameLayout mAdView = view.findViewById(R.id.adContainer);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConnectivityManager mgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.isConnected()) {
                loadBanner(view);
            }
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        /*String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");
        if (data.equals("none")) {
            profileId = firebaseUser.getUid();
        } else {
            //profileId = firebaseUser.getUid()
            profileId = data;
        }*/

        root_ref = FirebaseDatabase.getInstance().getReference();

        pbar = view.findViewById(R.id.pbar);
        inbox_list = view.findViewById(R.id.inboxlist);


        inbox_list = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inbox_list.setLayoutManager(layout);
        inbox_list.setHasFixedSize(false);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppGlobal.hideSoftKeyboard(getActivity());

            }
        });

        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });


        isview_created = true;
        getCurrentUserData();
        getData();
    }

    User currentUser;
    private void getCurrentUserData() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // on start we will get the Inbox Message of user  which is show in bottom list of third tab

    private List<String> followingList;
    public void getData() {

        followingList = new ArrayList<>();
        pbar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                pbar.setVisibility(View.GONE);
                if(dataSnapshot.getChildrenCount() < 1){
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        followingList.add(snapshot.getKey());
                    }
                    if (followingList.size() > 0) {
                        getListOfUsers();
                    } else {
                        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<User> listUsers = new ArrayList<>();
    private void getListOfUsers() {
        listUsers = new ArrayList<>();

        pbar.setVisibility(View.VISIBLE);
        view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);

        root_ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String followList : followingList) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (followList.equalsIgnoreCase(user.getId())) {
                            listUsers.add(user);
                        }
                    }
                }

                if (listUsers.size() > 0) {
                    pbar.setVisibility(View.GONE);
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    SelectUserAdapter mAdapter = new SelectUserAdapter(getActivity(), listUsers, new OnItemClick<User>() {
                        @Override
                        public void onItemClick(User data, int position) {
                            pbar.setVisibility(View.VISIBLE);
                            ChatFunctions.insertInbox(root_ref,
                                    firebaseUser.getUid(),
                                    currentUser.getName(),
                                    currentUser.getImageurl(),
                                    data.getId(),
                                    data.getBusiness_name(),
                                    data.getImageurl(),
                                    "2", new ChatFunctions.OnChatThreadCreated() {
                                @Override
                                public void onChatThreadCratedSuccess() {
                                    pbar.setVisibility(View.GONE);
                                    getActivity().onBackPressed();
                                }

                                @Override
                                public void onChatThreadCratedFail(String msg) {
                                    pbar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    inbox_list.setAdapter(mAdapter);
                } else {
                    pbar.setVisibility(View.GONE);
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //open the chat fragment and on item click and pass your id and the other person id in which
    //you want to chat with them and this parameter is that is we move from match list or inbox list
    public void chatFragment(String receiverid, String name, String picture) {
        /*Chat_Activity chat_activity = new Chat_Activity(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

        Bundle args = new Bundle();
        args.putString("user_id", receiverid);
        args.putString("user_name", name);
        args.putString("user_pic", picture);

        chat_activity.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Inbox_F, chat_activity).commit();*/
    }


    //this method will check there is a storage permission given or not
    private boolean check_ReadStoragepermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppGlobal.permission_Read_data);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }


}
