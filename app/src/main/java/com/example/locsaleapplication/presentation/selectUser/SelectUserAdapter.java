package com.example.locsaleapplication.presentation.selectUser;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.Inbox.InboxModel;
import com.example.locsaleapplication.utils.AppGlobal;
import com.example.locsaleapplication.utils.OnItemClick;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */
@SuppressWarnings("All")
public class SelectUserAdapter extends RecyclerView.Adapter<SelectUserAdapter.CustomViewHolder> {
    public Context context;
    private ArrayList<User> userList = new ArrayList<>();
    private OnItemClick<User> listenerItem;

    public SelectUserAdapter(Context context, ArrayList<User> userList, OnItemClick<User> listenerItem) {
        this.context = context;
        this.userList = userList;
        this.listenerItem = listenerItem;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_users, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView user_image;
        LinearLayout linearMainView;

        public CustomViewHolder(View view) {
            super(view);
            user_image = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.username);
            linearMainView = itemView.findViewById(R.id.mainlayout);
        }
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final User item = userList.get(i);
        holder.username.setText(item.getName());

        if (item.getImageurl() != null && !item.getImageurl().equals("")) {
            AppGlobal.loadImageUser(context, item.getImageurl(), 100, holder.user_image);
        } else {
            AppGlobal.loadImageUser(context, R.drawable.ic_profile, holder.user_image);
        }

        holder.linearMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerItem != null) {
                    listenerItem.onItemClick(i, item);
                }
            }
        });
    }
}