package com.example.locsaleapplication.presentation.chat;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.R;
import com.example.locsaleapplication.utils.AppGlobal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by AQEEL on 4/3/2018.
 */
@SuppressWarnings("All")
class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatGetSet> mDataSet;
    String myID;
    private static final int mychat = 1;
    private static final int friendchat = 2;
    private static final int mychatimage = 3;
    private static final int otherchatimage = 4;
    private static final int alert_message = 5;


    Context context;
    Integer today_day = 0;

    private OnItemClickListener listener;
    private OnLongClickListener long_listener;

    public interface OnItemClickListener {
        void onItemClick(ChatGetSet item, View view);
    }

    public interface OnLongClickListener {
        void onLongclick(ChatGetSet item, View view);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     *                Device id
     */

    ChatAdapter(List<ChatGetSet> dataSet, String id, Context context, OnItemClickListener listener, OnLongClickListener long_listener) {
        mDataSet = dataSet;
        this.myID = id;
        this.context = context;
        this.listener = listener;
        this.long_listener = long_listener;
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);

    }


    // this is the all types of view that is used in the chat
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View v = null;
        switch (viewtype) {
            // we have 4 type of layout in chat activity text chat of my and other and also
            // image layout of my and other
            case mychat:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_my, viewGroup, false);
                Chatviewholder mychatHolder = new Chatviewholder(v);
                return mychatHolder;
            case friendchat:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_other, viewGroup, false);
                Chatviewholder friendchatHolder = new Chatviewholder(v);
                return friendchatHolder;
            case mychatimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_my, viewGroup, false);
                Chatimageviewholder mychatimageHolder = new Chatimageviewholder(v);
                return mychatimageHolder;
            case otherchatimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_other, viewGroup, false);
                Chatimageviewholder otherchatimageHolder = new Chatimageviewholder(v);
                return otherchatimageHolder;
            case alert_message:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_alert, viewGroup, false);
                Alertviewholder alertviewholder = new Alertviewholder(v);
                return alertviewholder;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatGetSet chat = mDataSet.get(position);

        if (chat.getType().equals("text")) {
            Chatviewholder chatviewholder = (Chatviewholder) holder;
            // check if the message is from sender or receiver
            if (chat.getSender_id().equals(myID)) {
                if (chat.getStatus().equals("1"))
                    chatviewholder.message_seen.setText("Seen at " + ChangeDate_to_time(chat.getTime()));
                else
                    chatviewholder.message_seen.setText("Sent");

            } else {
                chatviewholder.message_seen.setText("");
            }
            // make the group of message by date set the gap of 1 min
            // means message send with in 1 min will show as a group
            if (position != 0) {
                ChatGetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {
                    chatviewholder.datetxt.setVisibility(View.GONE);
                } else {
                    chatviewholder.datetxt.setVisibility(View.VISIBLE);
                    chatviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }
                chatviewholder.message.setText(chat.getText());
            } else {
                chatviewholder.datetxt.setVisibility(View.VISIBLE);
                chatviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                chatviewholder.message.setText(chat.getText());
            }

            chatviewholder.bind(chat, long_listener);

        } else if (chat.getType().equals("image")) {
            final Chatimageviewholder chatimageholder = (Chatimageviewholder) holder;
            // check if the message is from sender or receiver
            if (chat.getSender_id().equals(myID)) {
                if (chat.getStatus().equals("1"))
                    chatimageholder.message_seen.setText("Seen at " + ChangeDate_to_time(chat.getTime()));
                else
                    chatimageholder.message_seen.setText("Sent");

            } else {
                chatimageholder.message_seen.setText("");
            }
            if (chat.getPic_url().equals("none")) {
                if (ChatActivity.uploading_image_id.equals(chat.getChat_id())) {
                    chatimageholder.p_bar.setVisibility(View.VISIBLE);
                    chatimageholder.message_seen.setText("");
                } else {
                    chatimageholder.p_bar.setVisibility(View.GONE);
                    chatimageholder.not_send_message_icon.setVisibility(View.VISIBLE);
                    chatimageholder.message_seen.setText("Not delivered. ");
                }
            } else {
                chatimageholder.not_send_message_icon.setVisibility(View.GONE);
                chatimageholder.p_bar.setVisibility(View.GONE);
            }

            // make the group of message by date set the gap of 1 min
            // means message send with in 1 min will show as a group
            if (position != 0) {
                ChatGetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {
                    chatimageholder.datetxt.setVisibility(View.GONE);
                } else {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                    chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }
                AppGlobal.loadImageChat(context, chat.getPic_url(), 400, chatimageholder.chatimage);
            } else {
                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                AppGlobal.loadImageChat(context, chat.getPic_url(), 400, chatimageholder.chatimage);


            }

            chatimageholder.bind(mDataSet.get(position), listener, long_listener);
        } else if (chat.getType().equals("delete")) {
            Alertviewholder alertviewholder = (Alertviewholder) holder;
            alertviewholder.message.setTextColor(context.getResources().getColor(R.color.delete_message_text));
            alertviewholder.message.setBackground(context.getResources().getDrawable(R.drawable.d_round_gray_background_2));

            alertviewholder.message.setText("This message is deleted by " + chat.getSender_name());

            if (position != 0) {
                ChatGetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(11, 13).equals(chat.getTimestamp().substring(11, 13))) {
                    alertviewholder.datetxt.setVisibility(View.GONE);
                } else {
                    alertviewholder.datetxt.setVisibility(View.VISIBLE);
                    alertviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }

            } else {
                alertviewholder.datetxt.setVisibility(View.VISIBLE);
                alertviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));

            }

        }


    }

    @Override
    public int getItemViewType(int position) {
        // get the type it view ( given message is from sender or receiver)
        if (mDataSet.get(position).getType().equals("text")) {
            if (mDataSet.get(position).sender_id.equals(myID)) {
                return mychat;
            }
            return friendchat;
        } else if (mDataSet.get(position).getType().equals("image")) {
            if (mDataSet.get(position).sender_id.equals(myID)) {
                return mychatimage;
            }

            return otherchatimage;

        } else {
            return alert_message;
        }
    }

    /**
     * Inner Class for a recycler view
     */

    // this is the all the viewholder in which first is for the text
    class Chatviewholder extends RecyclerView.ViewHolder {
        TextView message, datetxt, message_seen;
        View view;

        public Chatviewholder(View itemView) {
            super(itemView);
            view = itemView;
            this.message = view.findViewById(R.id.msgtxt);
            this.datetxt = view.findViewById(R.id.datetxt);
            message_seen = view.findViewById(R.id.message_seen);
        }

        public void bind(final ChatGetSet item, final OnLongClickListener long_listener) {
            message.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_listener.onLongclick(item, v);
                    return false;
                }
            });
        }
    }


    // second is for the chat image
    class Chatimageviewholder extends RecyclerView.ViewHolder {
        ImageView chatimage;
        TextView datetxt, message_seen;
        ProgressBar p_bar;
        ImageView not_send_message_icon;
        View view;

        public Chatimageviewholder(View itemView) {
            super(itemView);
            view = itemView;
            this.chatimage = view.findViewById(R.id.chatimage);
            this.datetxt = view.findViewById(R.id.datetxt);
            message_seen = view.findViewById(R.id.message_seen);
            not_send_message_icon = view.findViewById(R.id.not_send_messsage);
            p_bar = view.findViewById(R.id.p_bar);
        }

        public void bind(final ChatGetSet item, final OnItemClickListener listener, final OnLongClickListener long_listener) {

            chatimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, v);
                }
            });

            chatimage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_listener.onLongclick(item, v);
                    return false;
                }
            });
        }

    }

    // forth is for the alert type view
    class Alertviewholder extends RecyclerView.ViewHolder {
        TextView message, datetxt;
        View view;

        public Alertviewholder(View itemView) {
            super(itemView);
            view = itemView;
            this.message = view.findViewById(R.id.message);
            this.datetxt = view.findViewById(R.id.datetxt);
        }

    }


    // change the date into (today ,yesterday and date)
    /*public String ChangeDate(String date) {

        try {
            long currenttime = System.currentTimeMillis();

            //database date in millisecond
            long databasedate = 0;
            Date d = null;
            try {
                d = AppGlobal.df.parse(date);
                databasedate = d.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = currenttime - databasedate;
            if (difference < 86400000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if (today_day == chatday)
                    return "Today " + sdf.format(d);
                else if ((today_day - chatday) == 1)
                    return "Yesterday " + sdf.format(d);
            } else if (difference < 172800000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if ((today_day - chatday) == 1)
                    return "Yesterday " + sdf.format(d);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
            return sdf.format(d);
        } catch (Exception e) {

        } finally {

            return "";
        }

    }*/

    public String ChangeDate(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = AppGlobal.df.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }



    // change the date into (today ,yesterday and date)
    public String ChangeDate_to_time(String date) {
        try {
            Date d = null;
            try {
                d = AppGlobal.df2.parse(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            return sdf.format(d);
        } catch (Exception e) {

        } finally {
            return "";
        }

    }


    // get the audio file duration that is store in our directory
    public String getfileduration(Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            long second = (file_duration / 1000) % 60;
            long minute = (file_duration / (1000 * 60)) % 60;

            return String.format("%02d:%02d", minute, second);
        } catch (Exception e) {

        }
        return null;
    }


}
