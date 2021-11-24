package com.example.locsaleapplication.Model;

public class User {

    private String name;
    private String email;
    private String username;
    private String bio;
    private String imageurl;
    private String id;
    private String type;
    private String token;
    private String business_name;
    private String business_field;
    private String business_sub_category;
    private String contact_number;
    private String address;

    public User() {
    }

    public User(String name, String email, String username, String bio,
                String imageurl, String id, String type, String token,
                String business_name, String business_field, String business_sub_category, String contact_number, String address) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.imageurl = imageurl;
        this.id = id;
        this.type = type;
        this.token = token;
        this.business_name = business_name;
        this.business_field = business_field;
        this.business_sub_category = business_sub_category;
        this.contact_number = contact_number;
        this.address = address;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_field() {
        return business_field;
    }

    public void setBusiness_field(String business_field) {
        this.business_field = business_field;
    }

    public String getBusiness_sub_category() {
        return business_sub_category;
    }

    public void setBusiness_sub_category(String business_sub_category) {
        this.business_sub_category = business_sub_category;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
