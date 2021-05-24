package com.example.locsaleapplication.Model;

public class Post {

    private String description;
    private String imageUrl;
    private String postId;
    private String publisher;
    private String type;

    public Post() {
    }

    public Post(String description, String imageUrl, String postId, String publisher, String type) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publisher = publisher;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
