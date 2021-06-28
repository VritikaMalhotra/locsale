package com.example.locsaleapplication.Adapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.FollowerDetailActivity;
import com.example.locsaleapplication.FollowersActivity;
import com.example.locsaleapplication.Fragments.ProfileFragment;
import com.example.locsaleapplication.MainActivity;
import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.SendNotification;
import com.example.locsaleapplication.SendNotificationFromUA;
import com.example.locsaleapplication.ShopkeeperDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowerUserAdapter extends RecyclerView.Adapter<FollowerUserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;
    private FirebaseUser firebaseUser;
    public static String token;

    public FollowerUserAdapter(Context mContext, List<User> mUsers, boolean isFragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new FollowerUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUsers.get(position);
        holder.btnfollow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getUsername());
        holder.name.setText(user.getName());

        Picasso.get().load(user.getImageurl()).resize(300,300).placeholder(R.drawable.ic_profile).into(holder.imageProfile);

        holder.btnfollow.setVisibility(View.GONE);

    }




    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView name;
        public Button btnfollow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            name = itemView.findViewById(R.id.fullname);
            btnfollow = itemView.findViewById(R.id.btn_follow);

        }
    }

    //Upadted by Vritika
    private void addNotification(String UserId) {

        final String[] username = new String[1];
        FirebaseDatabase.getInstance().getReference().child("Users").child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                token = user.getToken();
                username[0] = user.getUsername();
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

        Intent myIntent = new Intent(mContext, SendNotificationFromUA.class);
        mContext.startActivity(myIntent);
    }

}