package com.example.locsaleapplication.presentation.Inbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.R;
import com.example.locsaleapplication.utils.AppGlobal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AQEEL on 3/20/2018.
 */
@SuppressWarnings("All")
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.CustomViewHolder> {
    public Context context;
    ArrayList<InboxModel> inbox_dataList = new ArrayList<>();
    ArrayList<InboxModel> inbox_dataList_filter = new ArrayList<>();
    private OnItemClickListener listener;
    private OnLongItemClickListener longlistener;

    Integer today_day = 0;

    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(InboxModel item);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(InboxModel item);
    }

    public InboxAdapter(Context context, ArrayList<InboxModel> user_dataList, OnItemClickListener listener, OnLongItemClickListener longlistener) {
        this.context = context;
        this.inbox_dataList = user_dataList;
        this.inbox_dataList_filter = user_dataList;
        this.listener = listener;
        this.longlistener = longlistener;

        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inbox_list, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return inbox_dataList_filter.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView username, last_message, date_created;
        ImageView user_image;

        public CustomViewHolder(View view) {
            super(view);
            user_image = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.username);
            last_message = itemView.findViewById(R.id.message);
            date_created = itemView.findViewById(R.id.datetxt);
        }

        public void bind(final InboxModel item, final OnItemClickListener listener, final OnLongItemClickListener longItemClickListener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final InboxModel item = inbox_dataList_filter.get(i);
        holder.username.setText(item.getSellerName());
        Date date = new Date(item.getTimestamp());
        holder.date_created.setText(AppGlobal.convertNormalDateToTimeAgo(context, date));

        if (item.getSellerPic() != null && !item.getSellerPic().equals(""))
            AppGlobal.loadImageUser(context, item.getSellerPic(), 100, holder.user_image);

        holder.bind(item, listener, longlistener);
    }
}