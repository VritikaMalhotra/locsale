package com.example.locsaleapplication.presentation.Inbox;


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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.chat.ChatActivity;
import com.example.locsaleapplication.presentation.chat.commons.Fragment_Callback;
import com.example.locsaleapplication.presentation.selectUser.SelectUserFragment;
import com.example.locsaleapplication.utils.AppGlobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("All")
public class InboxFragment extends Fragment {


    View view;
    Context context;

    RecyclerView inbox_list;

    ArrayList<InboxModel> inbox_arraylist;
    DatabaseReference root_ref;

    InboxAdapter mInboxAdapter;

    ProgressBar pbar;

    boolean isview_created = false;
    private FirebaseUser firebaseUser;
    //private String profileId;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
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

        // intialize the arraylist and and inboxlist
        inbox_arraylist = new ArrayList<>();

        inbox_list = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inbox_list.setLayoutManager(layout);
        inbox_list.setHasFixedSize(false);
        mInboxAdapter = new InboxAdapter(context, inbox_arraylist, new InboxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InboxModel item) {

                // if user allow the stroage permission then we open the chat view
                if (check_ReadStoragepermission())
                    chatFragment(item.getId(), item.getSellerId(), item.getSellerName(), item.getSellerPic());


            }
        }, new InboxAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(InboxModel item) {

            }
        });

        inbox_list.setAdapter(mInboxAdapter);


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

        view.findViewById(R.id.btnGetUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SelectUserFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        isview_created = true;

        getData();
    }

    // on start we will get the Inbox Message of user  which is show in bottom list of third tab
    ValueEventListener eventListener2;
    Query inbox_query;

    public void getData() {
        pbar.setVisibility(View.VISIBLE);
        inbox_query = root_ref.child("Inbox");
        eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    InboxModel model = ds.getValue(InboxModel.class);
                    model.setId(ds.getKey());
                    if (model.getId().contains(firebaseUser.getUid())) {
                        inbox_arraylist.add(model);
                    }
                }

                pbar.setVisibility(View.GONE);

                if (inbox_arraylist.isEmpty())
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                else {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    Collections.reverse(inbox_arraylist);
                    mInboxAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener2);
    }


    // on stop we will remove the listener
    @Override
    public void onStop() {
        super.onStop();
        if (inbox_query != null)
            inbox_query.removeEventListener(eventListener2);
    }


    //open the chat fragment and on item click and pass your id and the other person id in which
    //you want to chat with them and this parameter is that is we move from match list or inbox list
    public void chatFragment(String threadId, String receiverid, String name, String picture) {
        ChatActivity chat_activity = new ChatActivity(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

        Bundle args = new Bundle();
        args.putString("thread_id", threadId);
        args.putString("user_id", receiverid);
        args.putString("user_name", name);
        args.putString("user_pic", picture);

        chat_activity.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Inbox_F, chat_activity).commit();
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
