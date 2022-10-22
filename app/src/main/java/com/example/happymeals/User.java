package com.example.happymeals;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class User {
    private String u_id;
    private String username;
    private FirebaseFirestore conn;
    CollectionReference userRef;

    private String get_u_id(String username) {
        // Search up database to get the u_id for the user with username = 'username'
        return "";
    }

    public User() {
        u_id = "Fw8E3ba2rjfSmuOPxxHE"; // Default generated right now (temporary)
        username = "Guest"; // This is temporary
        conn = FirebaseFirestore.getInstance();
        userRef = conn.collection("users");
    }

    public User(String username) {
        // login existing user
        this.username = username;
        conn = FirebaseFirestore.getInstance();
        u_id = this.get_u_id(this.username);
    }

    public void newUser(String username) {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        userRef.add(data);
    }



    public String getU_id() {
        return this.u_id;
    }

    public String getUsername() {
        return this.username;
    }

    public FirebaseFirestore getConn() {
        return this.conn;
    }

//    public static User createUser(String username) {
//        // Create a new user in the database
//        // Create a User object for that user
//        // Return that user
//
//
//    }



}
