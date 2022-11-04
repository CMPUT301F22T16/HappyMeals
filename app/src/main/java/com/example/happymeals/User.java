package com.example.happymeals;
public class User {

    private String username;

    //TEMPORARY TO BE ONLY USED FOR HALFWAY CHECKPOINT
    public User() {
        this.username = "Guest";
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
