package com.example.happymeals;

import java.util.UUID;

public class Storage {

    private String storName;
    private String id;

    public Storage(String storName) {
        this.storName = storName;
        this.id = UUID.randomUUID().toString();
    }

    public Storage(String storName, String id) {
        this.storName = storName;
        this.id = id;
    }

    public String getStorName() {
        return storName;
    }

    //NOTE: Do not use these methods, they are for querying purposes only.
    public String getId() {
        return id;
    }
}
