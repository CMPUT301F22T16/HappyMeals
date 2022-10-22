package com.example.happymeals;

import org.junit.Test;

public class FireAdapterTest {

    @Test
    public void testAddUser() {
        FireAdapter db = new FireAdapter();
        db.addUser("Mohammad");
    }
}
