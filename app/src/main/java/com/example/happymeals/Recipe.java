package com.example.happymeals;

import java.util.HashMap;

public class Recipe implements Storable{
    private String r_id;

    public String get_r_id() {
        return this.r_id;
    }

    @Override
    public HashMap<String, Object> getStorable() {
        return null;
    }
}
