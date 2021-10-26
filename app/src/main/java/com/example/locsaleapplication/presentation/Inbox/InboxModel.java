package com.example.locsaleapplication.presentation.Inbox;

/**
 * Created by AQEEL on 9/11/2018.
 */

public class InboxModel {

    String id,buyerId,buyerName,buyerPic,sellerId,sellerName,sellerPic,currentUsertype;
    long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPic() {
        return buyerPic;
    }

    public void setBuyerPic(String buyerPic) {
        this.buyerPic = buyerPic;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPic() {
        return sellerPic;
    }

    public void setSellerPic(String sellerPic) {
        this.sellerPic = sellerPic;
    }

    public String getCurrentUsertype() {
        return currentUsertype;
    }

    public void setCurrentUsertype(String currentUsertype) {
        this.currentUsertype = currentUsertype;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
