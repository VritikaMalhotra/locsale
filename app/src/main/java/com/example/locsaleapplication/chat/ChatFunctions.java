package com.example.locsaleapplication.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class ChatFunctions {
    public static void insertInbox(DatabaseReference mRootRef,
                                   String buyerId, String buyerName, String buyerPic,
                                   String sellerId, String sellerName, String sellerPic,
                                   String currentUsertype, OnChatThreadCreated callBack) {
        HashMap<String, Object> mapInBoxCreated = new HashMap<>();
        mapInBoxCreated.put("buyerId", buyerId);
        mapInBoxCreated.put("buyerName", buyerName);
        mapInBoxCreated.put("buyerPic", buyerPic);
        mapInBoxCreated.put("sellerId", sellerId);
        mapInBoxCreated.put("sellerName", sellerName);
        mapInBoxCreated.put("sellerPic", sellerPic);
        mapInBoxCreated.put("currentUserId", currentUsertype);
        mapInBoxCreated.put("timestamp", ServerValue.TIMESTAMP);

        mRootRef.child("Inbox").child(buyerId + "-" + sellerId).setValue(mapInBoxCreated).addOnCompleteListener(task -> {
            callBack.onChatThreadCratedSuccess();
        }).addOnFailureListener(e -> {
            callBack.onChatThreadCratedFail(e.getMessage());
        });
    }

    public interface OnChatThreadCreated {
        void onChatThreadCratedSuccess();
        void onChatThreadCratedFail(String msg);
    }
}
