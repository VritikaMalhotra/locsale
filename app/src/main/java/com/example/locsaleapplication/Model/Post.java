package com.example.locsaleapplication.Model;

public class Post {

    private String description;
    private String imageUrl;
    private String postId;
    private String publisher;
    private String type;
    private String stAudienceName;
    private String stLocationLat;
    private String stLocationLng;
    private String stLocationName;
    private String stLocationType;
    private String stRadius;
    private long timestamp;

    public Post() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStAudienceName() {
        return stAudienceName;
    }

    public void setStAudienceName(String stAudienceName) {
        this.stAudienceName = stAudienceName;
    }

    public String getStLocationLat() {
        return stLocationLat;
    }

    public void setStLocationLat(String stLocationLat) {
        this.stLocationLat = stLocationLat;
    }

    public String getStLocationLng() {
        return stLocationLng;
    }

    public void setStLocationLng(String stLocationLng) {
        this.stLocationLng = stLocationLng;
    }

    public String getStLocationName() {
        return stLocationName;
    }

    public void setStLocationName(String stLocationName) {
        this.stLocationName = stLocationName;
    }

    public String getStLocationType() {
        return stLocationType;
    }

    public void setStLocationType(String stLocationType) {
        this.stLocationType = stLocationType;
    }

    public String getStRadius() {
        return stRadius;
    }

    public void setStRadius(String stRadius) {
        this.stRadius = stRadius;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
