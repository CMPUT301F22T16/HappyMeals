package com.example.happymeals;

import com.example.happymeals.storage.Storage;

import org.jetbrains.annotations.TestOnly;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class StorageTest {

    Storage storage;

    @Before
    public void mockStorage() {
        String name = "Fridge";
        Storage storage = new Storage(name);
        this.storage = storage;
    }

    @Test
    public void testGetStoreName() {
        String name = this.storage.getStoreName();
        Assert.assertEquals(name, "Fridge");
    }

    @Test
    public void testSetStoreName() {
        String new_name = "Freezer";
        this.storage.setStoreName(new_name);
        Assert.assertEquals(this.storage.getStoreName(), new_name);
    }

    @Test
    public void testGetItemCount() {
        Assert.assertEquals(this.storage.getItemCount(), 0);
    }

    @Test
    public void testSetItemCount() {
        this.storage.setItemCount(1);
        Assert.assertEquals(this.storage.getItemCount(), 1);
    }

    @Test
    public void testGetStorable() {
        this.storage.setItemCount(1);
        this.storage.setStoreName("Freezer");

        HashMap<String, Object> data = this.storage.getStorable();
        String name = (String) data.get("type");
        int itemcount = (Integer) data.get("items");

        Assert.assertEquals(name, "Freezer");
        Assert.assertEquals(itemcount, 1);
    }
}
