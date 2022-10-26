package com.example.happymeals.recipe;


public class Ingredient {
    private String desc;
    private String category;

    public Ingredient(String desc) {
        this.desc = desc;
        this.category = null;
    }

    public Ingredient(String desc, String category) {
        this.desc = desc;
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
        return category;
    }
}
