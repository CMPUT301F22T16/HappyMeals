package com.example.happymeals;

import com.google.firebase.firestore.QuerySnapshot;

public interface SynchronousListener {
    public void onStart();
    public void onSuccess(QuerySnapshot data);
    public void onFailed(Exception e);
}
