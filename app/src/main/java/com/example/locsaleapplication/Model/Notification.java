package com.example.locsaleapplication.Model;

public class Notification {

    private String userid;
    private String test;
    private String postid;
    private boolean isPost;

    public Notification() {
    }

    public Notification(String userid, String test, String postid, boolean isPost) {
        this.userid = userid;
        this.test = test;
        this.postid = postid;
        this.isPost = isPost;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }
}
