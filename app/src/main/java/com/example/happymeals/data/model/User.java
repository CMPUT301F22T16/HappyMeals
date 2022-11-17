package com.example.happymeals.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {

    private String firstName;
    private String lastName;
    private String userName;

    public User(String firstName, String lastName, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}